package com.cailu.bom.erp.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateOutboundRequest(
        String customNo,
        @NotNull LocalDate billDate,
        @NotBlank String outboundType,
        String orderNo,
        String orderCustomNo,
        LocalDate orderDate,
        String orderType,
        @NotBlank String customerCode,
        @NotBlank String customerName,
        String contact,
        String mobile,
        String community,
        @NotBlank String address,
        String channel,
        String customerLevel,
        @NotBlank String salesDept,
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
        @NotEmpty @Valid List<Item> items
) {
    public record Item(
            String position,
            Long productId,
            @NotBlank String sku,
            String productName,
            String brand,
            String spec,
            String grade,
            String batchNo,
            BigDecimal setsQty,
            BigDecimal boxes,
            BigDecimal pieces,
            @NotNull @DecimalMin("0.0001") BigDecimal qty,
            String unit,
            BigDecimal stdPrice,
            BigDecimal discount,
            @NotNull BigDecimal price,
            Long warehouseId,
            String warehouseCode,
            String warehouseName,
            String binName,
            String feeType,
            Boolean occupy,
            Boolean countPerf
    ) {
    }
}
