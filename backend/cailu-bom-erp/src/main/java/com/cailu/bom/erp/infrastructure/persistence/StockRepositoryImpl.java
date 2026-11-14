package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    public Optional<Stock> findByDimension(Long warehouseId, Long productId, String colorCode,
                                           String batchNo, String grade, String binName) {
        ErpStockDO row = erpStockMapper.selectOne(new LambdaQueryWrapper<ErpStockDO>()
                .eq(ErpStockDO::getWarehouseId, warehouseId)
                .eq(ErpStockDO::getProductId, productId)
                .eq(ErpStockDO::getColorCode, normalize(colorCode))
                .eq(ErpStockDO::getBatchNo, normalize(batchNo))
                .eq(ErpStockDO::getGrade, normalize(grade))
                .eq(ErpStockDO::getBinName, normalize(binName))
                .last("LIMIT 1 FOR UPDATE"));
        return Optional.ofNullable(row).map(this::toDomain);
    }

    @Override
    public List<String> suggestColorCodes(Long productId, String keyword) {
        return erpStockMapper.selectColorCodeSuggestions(productId, keyword == null ? "" : keyword.trim());
    }

    @Override
    public List<Stock> balancesAtDate(Long warehouseId, LocalDate billDate) {
        return erpStockLedgerMapper.selectBalancesAtDate(warehouseId, billDate).stream()
                .map(row -> new Stock(null, row.warehouseId(), row.productId(), row.colorCode(),
                        row.batchNo(), row.grade(), row.binName(), row.quantity(), BigDecimal.ZERO))
                .toList();
    }

    @Override
    public Stock save(Stock stock) {
        ErpStockDO row = new ErpStockDO();
        row.setId(stock.getId());
        row.setWarehouseId(stock.getWarehouseId());
        row.setProductId(stock.getProductId());
        row.setColorCode(stock.getColorCode());
        row.setBatchNo(stock.getBatchNo());
        row.setGrade(stock.getGrade());
        row.setBinName(stock.getBinName());
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
    public Stock increase(Long warehouseId, Long productId, String colorCode, String batchNo,
                          String grade, String binName, BigDecimal qty,
                          String bizType, Long bizId, LocalDate bizDate, String remark) {
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("increase qty must be positive");
        }
        String color = normalize(colorCode);
        String batch = normalize(batchNo);
        String productGrade = normalize(grade);
        String bin = normalize(binName);
        Stock current = findByDimension(warehouseId, productId, color, batch, productGrade, bin)
                .orElse(new Stock(null, warehouseId, productId, color, batch, productGrade, bin,
                        BigDecimal.ZERO, BigDecimal.ZERO));
        Stock updated = save(current.increase(qty));
        appendLedger(warehouseId, productId, color, batch, productGrade, bin,
                bizType, bizId, bizDate, qty, updated.getQuantity(), remark);
        return updated;
    }

    @Override
    public Stock decrease(Long warehouseId, Long productId, String colorCode, String batchNo,
                          String grade, String binName, BigDecimal qty,
                          String bizType, Long bizId, LocalDate bizDate, String remark) {
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("decrease qty must be positive");
        }
        String color = normalize(colorCode);
        String batch = normalize(batchNo);
        String productGrade = normalize(grade);
        String bin = normalize(binName);
        Stock current = findByDimension(warehouseId, productId, color, batch, productGrade, bin)
                .orElseThrow(() -> new IllegalArgumentException("stock not found for selected dimensions"));
        Stock updated = save(current.decrease(qty));
        appendLedger(warehouseId, productId, color, batch, productGrade, bin,
                bizType, bizId, bizDate, qty.negate(), updated.getQuantity(), remark);
        return updated;
    }

    private void appendLedger(Long warehouseId, Long productId, String colorCode, String batchNo,
                              String grade, String binName, String bizType, Long bizId, LocalDate bizDate,
                              BigDecimal changeQty, BigDecimal balanceQty, String remark) {
        ErpStockLedgerDO ledger = new ErpStockLedgerDO();
        ledger.setWarehouseId(warehouseId);
        ledger.setProductId(productId);
        ledger.setColorCode(colorCode);
        ledger.setBatchNo(batchNo);
        ledger.setGrade(grade);
        ledger.setBinName(binName);
        ledger.setBizType(bizType);
        ledger.setBizDate(bizDate);
        ledger.setBizId(bizId);
        ledger.setChangeQty(changeQty);
        ledger.setBalanceQty(balanceQty);
        ledger.setRemark(remark);
        erpStockLedgerMapper.insert(ledger);
    }

    private Stock toDomain(ErpStockDO row) {
        return new Stock(row.getId(), row.getWarehouseId(), row.getProductId(),
                row.getColorCode(), row.getBatchNo(), row.getGrade(), row.getBinName(),
                row.getQuantity(), row.getLockedQty());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
