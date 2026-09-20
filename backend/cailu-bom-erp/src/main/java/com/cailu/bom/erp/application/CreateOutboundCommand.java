package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateOutboundCommand(
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
        BigDecimal totalSqm,
        BigDecimal totalWeight,
        List<Item> items
) {
    public record Item(
            String position,
            Long productId,
            String sku,
            String productName,
            String brand,
            String spec,
            String grade,
            String batchNo,
            BigDecimal setsQty,
            BigDecimal boxes,
            BigDecimal pieces,
            BigDecimal qty,
            String unit,
            BigDecimal stdPrice,
            BigDecimal discount,
            BigDecimal price,
            Long warehouseId,
            String warehouseCode,
            String warehouseName,
            String binName,
            String feeType,
            boolean occupy,
            boolean countPerf
    ) {
    }

    public CreateOutboundCommand {
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
    }
}
