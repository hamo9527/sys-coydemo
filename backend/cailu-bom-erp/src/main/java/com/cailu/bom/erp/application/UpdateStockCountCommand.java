package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.util.List;

public record UpdateStockCountCommand(List<Item> items) {
    public UpdateStockCountCommand {
        if (items == null) {
            throw new IllegalArgumentException("count items required");
        }
        items = List.copyOf(items);
    }

    public record Item(Long productId, String colorCode, String batchNo, String grade,
                       String binName, BigDecimal costPrice, BigDecimal actualQty) {
        public Item {
            if (productId == null || productId <= 0) {
                throw new IllegalArgumentException("valid product id required");
            }
            if (costPrice == null || costPrice.signum() < 0) {
                throw new IllegalArgumentException("non-negative cost price required");
            }
            if (actualQty != null && actualQty.signum() < 0) {
                throw new IllegalArgumentException("non-negative actual quantity required");
            }
        }
    }
}
