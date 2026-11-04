package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("erp_product")
public class ErpProductDO extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String sku;
    private String mnemonicCode;
    private String name;
    private String brandName;
    private String category;
    private String spec;
    private String unit;
    private String auxiliaryUnit;
    private String auxiliarySpec;
    private String packageUnit;
    private String packageSpec;
    private BigDecimal unitWeight;
    private BigDecimal minOrderQty;
    private BigDecimal minStock;
    private BigDecimal maxStock;
    private BigDecimal smallColorStock;
    private Integer stockAgeThreshold;
    private Integer slowMovingDays;
    private Integer shelfLifeDays;
    private String productAttribute;
    private Boolean virtualProduct;
    private Boolean installationIncluded;
    private Boolean cloudStoreVisible;
    private String productionProcess;
    private String productionOrigin;
    private String usageLocation;
    private String vrUrl;
    private String remark;

    private BigDecimal purchasePrice;
    private BigDecimal salePrice;

    /** 0-disabled 1-enabled */
    private Integer status;
}
