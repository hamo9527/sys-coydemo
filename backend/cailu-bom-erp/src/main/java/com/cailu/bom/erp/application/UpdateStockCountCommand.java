package com.cailu.bom.erp.application;

import java.math.BigDecimal;
import java.util.List;

public record UpdateStockCountCommand(List<Item> items) {
    public UpdateStockCountCommand {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("count items required");
        }
    }

    public record Item(Long itemId, BigDecimal actualQty) {
        public Item {
            if (itemId == null || actualQty == null || actualQty.signum() < 0) {
                throw new IllegalArgumentException("item id and non-negative actual quantity required");
            }
        }
    }
}
