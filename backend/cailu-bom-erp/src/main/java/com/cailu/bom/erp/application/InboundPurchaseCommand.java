package com.cailu.bom.erp.application;

import java.util.List;

public record InboundPurchaseCommand(List<Item> items) {

    public InboundPurchaseCommand {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("inbound items required");
        }
    }

    public record Item(Long itemId, String colorCode, String batchNo, String grade, String binName) {
        public Item {
            if (itemId == null) {
                throw new IllegalArgumentException("purchase item id required");
            }
        }
    }
}
