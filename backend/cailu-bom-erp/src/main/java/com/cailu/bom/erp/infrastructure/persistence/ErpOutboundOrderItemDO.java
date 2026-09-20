package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("erp_outbound_order_item")
public class ErpOutboundOrderItemDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long orderId;
    private Integer lineNo;
    private String position;
    private Long productId;
    private String sku;
    private String productName;
    private String brand;
    private String spec;
    private String grade;
    private String batchNo;
    private BigDecimal setsQty;
    private BigDecimal boxes;
    private BigDecimal pieces;
    private BigDecimal qty;
    private String unit;
    private BigDecimal stdPrice;
    private BigDecimal discount;
    private BigDecimal price;
    private BigDecimal amount;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String binName;
    private String feeType;
    private Integer occupy;
    private Integer countPerf;
}
