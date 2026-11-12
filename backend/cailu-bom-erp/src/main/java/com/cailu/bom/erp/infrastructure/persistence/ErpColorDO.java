package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("erp_color")
public class ErpColorDO extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String code;
    private String name;
    private String shortName;
    private String brandName;
    private String seriesName;
    private String manufacturerColorCode;
    private String swatchImageUrl;
    private Integer status;
    private String remark;
}
