package com.cailu.bom.erp.interfaces;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String sku,
        String mnemonicCode,
        @NotBlank String name,
        @NotBlank String brandName,
        String category,
        String spec,
        @NotBlank String unit,
        String auxiliaryUnit,
        String auxiliarySpec,
        String packagingUnit,
        String packagingSpec,
        BigDecimal unitWeight,
        BigDecimal minimumOrderQty,
        BigDecimal minStock,
        BigDecimal maxStock,
        Boolean smallColorStock,
        Integer stockAgeThreshold,
        Integer obsoleteDays,
        Integer shelfLifeDays,
        @NotBlank String productAttribute,
        Boolean virtualProduct,
        Boolean installationRequired,
        Boolean cloudStoreVisible,
        String productionProcess,
        String productionOrigin,
        String usageLocation,
        String vrUrl,
        String remark,
        BigDecimal purchasePrice,
        BigDecimal salePrice
) {
}
