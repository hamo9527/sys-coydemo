package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class OutboundOrder {

    private final Long id;
    private final String billNo;
    private final String customNo;
    private final LocalDate billDate;
    private final String outboundType;
    private final String orderNo;
    private final String orderCustomNo;
    private final LocalDate orderDate;
    private final String orderType;
    private final String customerCode;
    private final String customerName;
    private final String contact;
    private final String mobile;
    private final String community;
    private final String address;
    private final String channel;
    private final String customerLevel;
    private final String salesDept;
    private final String salesperson;
    private final String guide;
    private final String designer;
    private final String designMethod;
    private final String shipFrom;
    private final String deliveryMethod;
    private final String packageProduct;
    private final String handler;
    private final String innerRemark;
    private final String outerRemark;
    private final String status;
    private final BigDecimal totalAmount;
    private final BigDecimal totalQty;
    private final BigDecimal totalBoxes;
    private final BigDecimal totalSqm;
    private final BigDecimal totalWeight;
    private final List<OutboundOrderItem> items;

    public OutboundOrder(Long id, String billNo, String customNo, LocalDate billDate, String outboundType,
                         String orderNo, String orderCustomNo, LocalDate orderDate, String orderType,
                         String customerCode, String customerName, String contact, String mobile,
                         String community, String address, String channel, String customerLevel,
                         String salesDept, String salesperson, String guide, String designer,
                         String designMethod, String shipFrom, String deliveryMethod, String packageProduct,
                         String handler, String innerRemark, String outerRemark, String status,
                         BigDecimal totalAmount, BigDecimal totalQty, BigDecimal totalBoxes,
                         BigDecimal totalSqm, BigDecimal totalWeight, List<OutboundOrderItem> items) {
        if (billNo == null || billNo.isBlank()) {
            throw new IllegalArgumentException("billNo required");
        }
        if (billDate == null) {
            throw new IllegalArgumentException("billDate required");
        }
        if (outboundType == null || outboundType.isBlank()) {
            throw new IllegalArgumentException("outboundType required");
        }
        if (customerCode == null || customerCode.isBlank()) {
            throw new IllegalArgumentException("customerCode required");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("customerName required");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("address required");
        }
        if (salesDept == null || salesDept.isBlank()) {
            throw new IllegalArgumentException("salesDept required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items required");
        }
        this.id = id;
        this.billNo = billNo;
        this.customNo = customNo;
        this.billDate = billDate;
        this.outboundType = outboundType;
        this.orderNo = orderNo;
        this.orderCustomNo = orderCustomNo;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.contact = contact;
        this.mobile = mobile;
        this.community = community;
        this.address = address;
        this.channel = channel;
        this.customerLevel = customerLevel;
        this.salesDept = salesDept;
        this.salesperson = salesperson;
        this.guide = guide;
        this.designer = designer;
        this.designMethod = designMethod;
        this.shipFrom = shipFrom;
        this.deliveryMethod = deliveryMethod;
        this.packageProduct = packageProduct;
        this.handler = handler;
        this.innerRemark = innerRemark;
        this.outerRemark = outerRemark;
        this.status = status == null ? "draft" : status;
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
        this.totalQty = totalQty == null ? BigDecimal.ZERO : totalQty;
        this.totalBoxes = totalBoxes == null ? BigDecimal.ZERO : totalBoxes;
        this.totalSqm = totalSqm == null ? BigDecimal.ZERO : totalSqm;
        this.totalWeight = totalWeight == null ? BigDecimal.ZERO : totalWeight;
        this.items = List.copyOf(items);
    }

    public OutboundOrder markEffective() {
        if (!"draft".equals(status)) {
            throw new IllegalArgumentException("only draft outbound orders can become effective");
        }
        return copyWithStatus("effective");
    }

    private OutboundOrder copyWithStatus(String nextStatus) {
        return new OutboundOrder(id, billNo, customNo, billDate, outboundType, orderNo, orderCustomNo,
                orderDate, orderType, customerCode, customerName, contact, mobile, community, address,
                channel, customerLevel, salesDept, salesperson, guide, designer, designMethod, shipFrom,
                deliveryMethod, packageProduct, handler, innerRemark, outerRemark, nextStatus,
                totalAmount, totalQty, totalBoxes, totalSqm, totalWeight, items);
    }
}
