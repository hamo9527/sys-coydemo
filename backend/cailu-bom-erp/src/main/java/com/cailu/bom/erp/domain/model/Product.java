package com.cailu.bom.erp.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
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
    private final String packageUnit;
    private final String packageSpec;
    private final BigDecimal unitWeight;
    private final BigDecimal minOrderQty;
    private final BigDecimal minStock;
    private final BigDecimal maxStock;
    private final BigDecimal smallColorStock;
    private final Integer stockAgeThreshold;
    private final Integer slowMovingDays;
    private final Integer shelfLifeDays;
    private final String productAttribute;
    private final Boolean virtualProduct;
    private final Boolean installationIncluded;
    private final Boolean cloudStoreVisible;
    private final String productionProcess;
    private final String productionOrigin;
    private final String usageLocation;
    private final String vrUrl;
    private final String remark;
    private final BigDecimal purchasePrice;
    private final BigDecimal salePrice;
    private final Integer status;
}
