package com.cailu.bom.erp.application;

public record ProductImportRow(
        String sku,
        String name,
        String brandName,
        String unit,
        String productAttribute,
        String mnemonicCode,
        String category,
        String spec,
        String remark
) {
}
