package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OutboundView(
        Long id,
        String billNo,
        String customNo,
        LocalDate billDate,
        String outboundType,
        String orderNo,
        String orderCustomNo,
        LocalDate orderDate,
        String orderType,
        String customerCode,
        String customerName,
        String contact,
        String mobile,
        String community,
        String address,
        String channel,
        String customerLevel,
        String salesDept,
        String salesperson,
        String guide,
        String designer,
        String designMethod,
        String shipFrom,
        String deliveryMethod,
        String packageProduct,
        String handler,
        String innerRemark,
        String outerRemark,
        String status,
        BigDecimal totalAmount,
        BigDecimal totalQty,
        BigDecimal totalBoxes,
        BigDecimal totalSqm,
        BigDecimal totalWeight,
        List<OutboundItemView> items
) {
}
