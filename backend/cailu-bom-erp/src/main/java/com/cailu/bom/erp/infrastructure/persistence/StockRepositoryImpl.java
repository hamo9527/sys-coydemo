package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final ErpStockMapper erpStockMapper;
    private final ErpStockLedgerMapper erpStockLedgerMapper;

    @Override
    public PageResult<Stock> page(Long warehouseId, Long productId, long page, long size) {
        long current = Math.max(page, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);
        LambdaQueryWrapper<ErpStockDO> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(ErpStockDO::getWarehouseId, warehouseId);
        }
        if (productId != null) {
            wrapper.eq(ErpStockDO::getProductId, productId);
        }
        wrapper.orderByDesc(ErpStockDO::getId);
        Page<ErpStockDO> result = erpStockMapper.selectPage(new Page<>(current, pageSize), wrapper);
        return new PageResult<>(
                result.getRecords().stream().map(this::toDomain).toList(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize());
    }

    @Override
    public Optional<Stock> findByWarehouseAndProduct(Long warehouseId, Long productId) {
        ErpStockDO row = erpStockMapper.selectOne(new LambdaQueryWrapper<ErpStockDO>()
                .eq(ErpStockDO::getWarehouseId, warehouseId)
                .eq(ErpStockDO::getProductId, productId)
                .last("LIMIT 1"));
        return Optional.ofNullable(row).map(this::toDomain);
    }

    @Override
    public Stock save(Stock stock) {
        ErpStockDO row = new ErpStockDO();
        row.setId(stock.getId());
        row.setWarehouseId(stock.getWarehouseId());
        row.setProductId(stock.getProductId());
        row.setQuantity(stock.getQuantity());
        row.setLockedQty(stock.getLockedQty());
        if (row.getId() == null) {
            erpStockMapper.insert(row);
        } else {
            erpStockMapper.updateById(row);
        }
        return toDomain(row);
    }

    @Override
    public Stock increase(Long warehouseId, Long productId, BigDecimal qty,
                          String bizType, Long bizId, String remark) {
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("increase qty must be positive");
        }
        Stock current = findByWarehouseAndProduct(warehouseId, productId)
                .orElse(new Stock(null, warehouseId, productId, BigDecimal.ZERO, BigDecimal.ZERO));
        Stock updated = save(current.increase(qty));

        ErpStockLedgerDO ledger = new ErpStockLedgerDO();
        ledger.setWarehouseId(warehouseId);
        ledger.setProductId(productId);
        ledger.setBizType(bizType);
        ledger.setBizId(bizId);
        ledger.setChangeQty(qty);
        ledger.setBalanceQty(updated.getQuantity());
        ledger.setRemark(remark);
        erpStockLedgerMapper.insert(ledger);

        return updated;
    }

    @Override
    public Stock decrease(Long warehouseId, Long productId, BigDecimal qty,
                          String bizType, Long bizId, String remark) {
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("decrease qty must be positive");
        }
        Stock current = findByWarehouseAndProduct(warehouseId, productId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));
        Stock updated;
        try {
            updated = save(current.decrease(qty));
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        ErpStockLedgerDO ledger = new ErpStockLedgerDO();
        ledger.setWarehouseId(warehouseId);
        ledger.setProductId(productId);
        ledger.setBizType(bizType);
        ledger.setBizId(bizId);
        ledger.setChangeQty(qty.negate());
        ledger.setBalanceQty(updated.getQuantity());
        ledger.setRemark(remark);
        erpStockLedgerMapper.insert(ledger);

        return updated;
    }

    private Stock toDomain(ErpStockDO row) {
        return new Stock(row.getId(), row.getWarehouseId(), row.getProductId(),
                row.getQuantity(), row.getLockedQty());
    }
}
