package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Append-only ledger; table has no updated_at / deleted columns.
 */
@Data
@TableName("erp_stock_ledger")
public class ErpStockLedgerDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long warehouseId;

    private Long productId;

    private String bizType;

    private Long bizId;

    private BigDecimal changeQty;

    private BigDecimal balanceQty;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
}
