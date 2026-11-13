package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.PurchaseOrder;
import com.cailu.bom.erp.domain.model.PurchaseOrderItem;
import com.cailu.bom.erp.domain.model.ProductColorSettings;
import com.cailu.bom.erp.domain.repository.ProductColorModeRepository;
import com.cailu.bom.erp.domain.repository.PurchaseOrderRepository;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseApplicationServiceImpl implements PurchaseApplicationService {

    private static final String BIZ_TYPE_PURCHASE_INBOUND = "purchase_inbound";

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductColorModeRepository productColorModeRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseView create(CreatePurchaseCommand command) {
        purchaseOrderRepository.findByOrderNo(command.orderNo()).ifPresent(o -> {
            throw new BusinessException("orderNo already exists");
        });
        List<PurchaseOrderItem> items = command.items().stream()
                .map(i -> {
                    validateDimensions(i.productId(), i.colorCode(), i.batchNo(), i.grade());
                    return new PurchaseOrderItem(null, i.productId(), i.colorCode(), i.batchNo(), i.grade(), i.binName(), i.qty(), i.price(), null);
                })
                .toList();
        BigDecimal total = items.stream()
                .map(PurchaseOrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PurchaseOrder order = new PurchaseOrder(null, command.orderNo(), command.supplierId(),
                command.warehouseId(), "draft", total, command.remark(), items);
        return toView(purchaseOrderRepository.save(order));
    }

    @Override
    public PurchaseView getById(Long id) {
        return toView(purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("purchase order not found", 404)));
    }

    @Override
    public PageResult<PurchaseView> list(long page, long size) {
        PageResult<PurchaseOrder> result = purchaseOrderRepository.page(page, size);
        return new PageResult<>(
                result.records().stream().map(this::toView).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseView inbound(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("purchase order not found", 404));
        PurchaseOrder completed;
        try {
            completed = order.markCompleted();
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ex.getMessage());
        }
        for (PurchaseOrderItem item : order.getItems()) {
            try {
                stockRepository.increase(
                        order.getWarehouseId(),
                        item.getProductId(),
                        item.getColorCode(),
                        item.getBatchNo(),
                        item.getGrade(),
                        item.getBinName(),
                        item.getQty(),
                        BIZ_TYPE_PURCHASE_INBOUND,
                        order.getId(),
                        order.getOrderNo());
            } catch (IllegalArgumentException ex) {
                throw new BusinessException(ex.getMessage());
            }
        }
        return toView(purchaseOrderRepository.updateStatus(completed.getId(), completed.getStatus()));
    }

    private void validateDimensions(Long productId, String colorCode, String batchNo, String grade) {
        ProductColorSettings settings = productColorModeRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("product not found", 404));
        if ("NONE".equals(settings.colorManagementMode()) && colorCode != null && !colorCode.isBlank()) {
            throw new BusinessException("该商品未启用色号管理");
        }
        if (!Boolean.TRUE.equals(settings.batchManagementEnabled()) && batchNo != null && !batchNo.isBlank()) {
            throw new BusinessException("该商品未启用批次管理");
        }
        if (!Boolean.TRUE.equals(settings.gradeManagementEnabled()) && grade != null && !grade.isBlank()) {
            throw new BusinessException("该商品未启用等级管理");
        }
    }

    private PurchaseView toView(PurchaseOrder order) {
        List<PurchaseItemView> items = order.getItems().stream()
                .map(i -> new PurchaseItemView(i.getId(), i.getProductId(), i.getColorCode(), i.getBatchNo(), i.getGrade(), i.getBinName(), i.getQty(), i.getPrice(), i.getAmount()))
                .toList();
        return new PurchaseView(order.getId(), order.getOrderNo(), order.getSupplierId(),
                order.getWarehouseId(), order.getStatus(), order.getTotalAmount(), order.getRemark(), items);
    }
}
