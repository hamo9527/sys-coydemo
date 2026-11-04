package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product {

    private final Long id;
    private final String sku;
    private final String mnemonicCode;
    private final String name;
    private final String brandName;
    private final String category;
    private final String spec;
    private final String unit;
    private final String auxiliaryUnit;
    private final String auxiliarySpec;
    private final String packagingUnit;
    private final String packagingSpec;
    private final BigDecimal unitWeight;
    private final BigDecimal minimumOrderQty;
    private final BigDecimal minStock;
    private final BigDecimal maxStock;
    private final Boolean smallColorStock;
    private final Integer stockAgeThreshold;
    private final Integer obsoleteDays;
    private final Integer shelfLifeDays;
    private final String productAttribute;
    private final Boolean virtualProduct;
    private final Boolean installationRequired;
    private final Boolean cloudStoreVisible;
    private final String productionProcess;
    private final String productionOrigin;
    private final String usageLocation;
    private final String vrUrl;
    private final String remark;
    private final BigDecimal purchasePrice;
    private final BigDecimal salePrice;
    private final Integer status;

    public Product(Long id, String sku, String mnemonicCode, String name, String brandName,
                   String category, String spec, String unit, String auxiliaryUnit, String auxiliarySpec,
                   String packagingUnit, String packagingSpec, BigDecimal unitWeight, BigDecimal minimumOrderQty,
                   BigDecimal minStock, BigDecimal maxStock, Boolean smallColorStock, Integer stockAgeThreshold,
                   Integer obsoleteDays, Integer shelfLifeDays, String productAttribute, Boolean virtualProduct,
                   Boolean installationRequired, Boolean cloudStoreVisible, String productionProcess,
                   String productionOrigin, String usageLocation, String vrUrl, String remark,
                   BigDecimal purchasePrice, BigDecimal salePrice, Integer status) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (brandName == null || brandName.isBlank()) {
            throw new IllegalArgumentException("brand name required");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("unit required");
        }
        if (productAttribute == null || productAttribute.isBlank()) {
            throw new IllegalArgumentException("product attribute required");
        }
        this.id = id;
        this.sku = sku;
        this.mnemonicCode = mnemonicCode;
        this.name = name;
        this.brandName = brandName;
        this.category = category;
        this.spec = spec;
        this.unit = unit;
        this.auxiliaryUnit = auxiliaryUnit;
        this.auxiliarySpec = auxiliarySpec;
        this.packagingUnit = packagingUnit;
        this.packagingSpec = packagingSpec;
        this.unitWeight = unitWeight;
        this.minimumOrderQty = minimumOrderQty;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.smallColorStock = smallColorStock == null ? false : smallColorStock;
        this.stockAgeThreshold = stockAgeThreshold;
        this.obsoleteDays = obsoleteDays;
        this.shelfLifeDays = shelfLifeDays;
        this.productAttribute = productAttribute;
        this.virtualProduct = virtualProduct == null ? false : virtualProduct;
        this.installationRequired = installationRequired == null ? false : installationRequired;
        this.cloudStoreVisible = cloudStoreVisible == null ? false : cloudStoreVisible;
        this.productionProcess = productionProcess;
        this.productionOrigin = productionOrigin;
        this.usageLocation = usageLocation;
        this.vrUrl = vrUrl;
        this.remark = remark;
        this.purchasePrice = purchasePrice == null ? BigDecimal.ZERO : purchasePrice;
        this.salePrice = salePrice == null ? BigDecimal.ZERO : salePrice;
        this.status = status == null ? 1 : status;
    }
}
