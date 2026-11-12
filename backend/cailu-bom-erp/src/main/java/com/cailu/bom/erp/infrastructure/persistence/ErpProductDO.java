package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String mnemonicCode;
    private String name;
    private String brandName;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String category;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String spec;
    private String unit;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String auxiliaryUnit;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String auxiliarySpec;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String packagingUnit;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String packagingSpec;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal unitWeight;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal minimumOrderQty;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal minStock;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal maxStock;
    private Boolean smallColorStock;
    private String colorManagementMode;
    private Boolean batchManagementEnabled;
    private Boolean gradeManagementEnabled;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer stockAgeThreshold;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer obsoleteDays;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer shelfLifeDays;
    private String productAttribute;
    private Boolean virtualProduct;
    private Boolean installationRequired;
    private Boolean cloudStoreVisible;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String productionProcess;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String productionOrigin;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String usageLocation;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String vrUrl;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String remark;

    private BigDecimal purchasePrice;
    private BigDecimal salePrice;

    /** 0-disabled 1-enabled */
    private Integer status;
}
