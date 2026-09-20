package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/** Line items have no soft-delete / audit columns in SQL. */
@Data
@TableName("erp_purchase_order_item")
public class ErpPurchaseOrderItemDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long orderId;

    private Long productId;

    private BigDecimal qty;

    private BigDecimal price;

    private BigDecimal amount;
}
