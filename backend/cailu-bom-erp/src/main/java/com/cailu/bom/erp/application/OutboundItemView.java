package com.cailu.bom.erp.application;

import java.math.BigDecimal;

public record OutboundItemView(
        Long id,
        Integer lineNo,
        String position,
        Long productId,
        String sku,
        String productName,
        String brand,
        String spec,
        String grade,
        String batchNo,
        BigDecimal setsQty,
        BigDecimal boxes,
        BigDecimal pieces,
        BigDecimal qty,
        String unit,
        BigDecimal stdPrice,
        BigDecimal discount,
        BigDecimal price,
        BigDecimal amount,
        Long warehouseId,
        String warehouseCode,
        String warehouseName,
        String binName,
        String feeType,
        boolean occupy,
        boolean countPerf
) {
}
