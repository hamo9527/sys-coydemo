package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.PurchaseOrder;
import com.cailu.bom.erp.domain.model.PurchaseOrderItem;
import com.cailu.bom.erp.domain.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PurchaseOrderRepositoryImpl implements PurchaseOrderRepository {

    private final ErpPurchaseOrderMapper erpPurchaseOrderMapper;
    private final ErpPurchaseOrderItemMapper erpPurchaseOrderItemMapper;

    @Override
    public Optional<PurchaseOrder> findById(Long id) {
        ErpPurchaseOrderDO row = erpPurchaseOrderMapper.selectById(id);
        if (row == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public Optional<PurchaseOrder> findByOrderNo(String orderNo) {
        ErpPurchaseOrderDO row = erpPurchaseOrderMapper.selectOne(new LambdaQueryWrapper<ErpPurchaseOrderDO>()
                .eq(ErpPurchaseOrderDO::getOrderNo, orderNo)
                .last("LIMIT 1"));
        if (row == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public PageResult<PurchaseOrder> page(long page, long size) {
        long current = Math.max(page, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);
        Page<ErpPurchaseOrderDO> result = erpPurchaseOrderMapper.selectPage(
                new Page<>(current, pageSize),
                new LambdaQueryWrapper<ErpPurchaseOrderDO>().orderByDesc(ErpPurchaseOrderDO::getId));
        List<PurchaseOrder> records = result.getRecords().stream()
                .map(row -> toDomain(row, loadItems(row.getId())))
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public PurchaseOrder save(PurchaseOrder order) {
        ErpPurchaseOrderDO row = new ErpPurchaseOrderDO();
        row.setId(order.getId());
        row.setOrderNo(order.getOrderNo());
        row.setSupplierId(order.getSupplierId());
        row.setWarehouseId(order.getWarehouseId());
        row.setStatus(order.getStatus());
        row.setTotalAmount(order.getTotalAmount());
        row.setRemark(order.getRemark());
        if (row.getId() == null) {
            erpPurchaseOrderMapper.insert(row);
            for (PurchaseOrderItem item : order.getItems()) {
                ErpPurchaseOrderItemDO itemRow = new ErpPurchaseOrderItemDO();
                itemRow.setOrderId(row.getId());
                itemRow.setProductId(item.getProductId());
                itemRow.setQty(item.getQty());
                itemRow.setPrice(item.getPrice());
                itemRow.setAmount(item.getAmount());
                erpPurchaseOrderItemMapper.insert(itemRow);
            }
        } else {
            erpPurchaseOrderMapper.updateById(row);
        }
        return toDomain(row, loadItems(row.getId()));
    }

    @Override
    public PurchaseOrder updateStatus(Long id, String status) {
        ErpPurchaseOrderDO row = erpPurchaseOrderMapper.selectById(id);
        if (row == null) {
            throw new IllegalArgumentException("purchase order not found");
        }
        row.setStatus(status);
        erpPurchaseOrderMapper.updateById(row);
        return toDomain(row, loadItems(row.getId()));
    }

    private List<PurchaseOrderItem> loadItems(Long orderId) {
        return erpPurchaseOrderItemMapper.selectList(new LambdaQueryWrapper<ErpPurchaseOrderItemDO>()
                        .eq(ErpPurchaseOrderItemDO::getOrderId, orderId)
                        .orderByAsc(ErpPurchaseOrderItemDO::getId))
                .stream()
                .map(item -> new PurchaseOrderItem(
                        item.getId(), item.getProductId(), item.getQty(), item.getPrice(), item.getAmount()))
                .toList();
    }

    private PurchaseOrder toDomain(ErpPurchaseOrderDO row, List<PurchaseOrderItem> items) {
        return new PurchaseOrder(row.getId(), row.getOrderNo(), row.getSupplierId(), row.getWarehouseId(),
                row.getStatus(), row.getTotalAmount(), row.getRemark(), items);
    }
}
