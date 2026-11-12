package com.cailu.bom.erp.infrastructure.persistence;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data @EqualsAndHashCode(callSuper=true) @TableName("erp_product_color")
public class ErpProductColorDO extends BaseEntity { @TableId(type=IdType.ASSIGN_ID) private Long id; private Long productId; private Long colorId; private Boolean allowInbound; private Boolean allowOutbound; private Integer status; private String remark; }