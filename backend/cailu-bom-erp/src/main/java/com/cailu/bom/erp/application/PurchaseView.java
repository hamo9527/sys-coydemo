package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseView(
        Long id,
        String orderNo,
        Long supplierId,
        Long warehouseId,
        String status,
        BigDecimal totalAmount,
        String remark,
        List<PurchaseItemView> items
) {
}
