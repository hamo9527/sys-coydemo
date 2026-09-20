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

    private String name;

    private String category;

    private String unit;

    private String spec;

    private BigDecimal purchasePrice;

    private BigDecimal salePrice;

    /** 0-disabled 1-enabled */
    private Integer status;
}
