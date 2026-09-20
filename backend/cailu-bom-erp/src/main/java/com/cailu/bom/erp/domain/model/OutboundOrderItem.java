package com.cailu.bom.erp.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OutboundOrderItem {

    private final Long id;
    private final Integer lineNo;
    private final String position;
    private final Long productId;
    private final String sku;
    private final String productName;
    private final String brand;
    private final String spec;
    private final String grade;
    private final String batchNo;
    private final BigDecimal setsQty;
    private final BigDecimal boxes;
    private final BigDecimal pieces;
    private final BigDecimal qty;
    private final String unit;
    private final BigDecimal stdPrice;
    private final BigDecimal discount;
    private final BigDecimal price;
    private final BigDecimal amount;
    private final Long warehouseId;
    private final String warehouseCode;
    private final String warehouseName;
    private final String binName;
    private final String feeType;
    private final boolean occupy;
    private final boolean countPerf;

    public OutboundOrderItem(Long id, Integer lineNo, String position, Long productId, String sku,
                             String productName, String brand, String spec, String grade, String batchNo,
                             BigDecimal setsQty, BigDecimal boxes, BigDecimal pieces, BigDecimal qty,
                             String unit, BigDecimal stdPrice, BigDecimal discount, BigDecimal price,
                             BigDecimal amount, Long warehouseId, String warehouseCode, String warehouseName,
                             String binName, String feeType, boolean occupy, boolean countPerf) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku required");
        }
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("qty must be positive");
        }
        this.id = id;
        this.lineNo = lineNo == null ? 1 : lineNo;
        this.position = position;
        this.productId = productId;
        this.sku = sku;
        this.productName = productName;
        this.brand = brand;
        this.spec = spec;
        this.grade = grade;
        this.batchNo = batchNo;
        this.setsQty = setsQty == null ? BigDecimal.ZERO : setsQty;
        this.boxes = boxes == null ? BigDecimal.ZERO : boxes;
        this.pieces = pieces == null ? BigDecimal.ZERO : pieces;
        this.qty = qty;
        this.unit = unit;
        this.stdPrice = stdPrice == null ? BigDecimal.ZERO : stdPrice;
        this.discount = discount == null ? BigDecimal.ZERO : discount;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.amount = amount != null ? amount : this.qty.multiply(this.price);
        this.warehouseId = warehouseId;
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
        this.binName = binName;
        this.feeType = feeType;
        this.occupy = occupy;
        this.countPerf = countPerf;
    }
}
