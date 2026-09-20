package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("erp_outbound_order")
public class ErpOutboundOrderDO extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String billNo;
    private String customNo;
    private LocalDate billDate;
    private String outboundType;
    private String orderNo;
    private String orderCustomNo;
    private LocalDate orderDate;
    private String orderType;
    private String customerCode;
    private String customerName;
    private String contact;
    private String mobile;
    private String community;
    private String address;
    private String channel;
    private String customerLevel;
    private String salesDept;
    private String salesperson;
    private String guide;
    private String designer;
    private String designMethod;
    private String shipFrom;
    private String deliveryMethod;
    private String packageProduct;
    private String handler;
    private String innerRemark;
    private String outerRemark;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal totalQty;
    private BigDecimal totalBoxes;
    private BigDecimal totalSqm;
    private BigDecimal totalWeight;
}
