package com.cailu.bom.erp.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InboundPurchaseRequest(
        @NotEmpty @Valid List<Item> items
) {
    public record Item(
            @NotNull Long itemId,
            String colorCode,
            String batchNo,
            String grade,
            String binName
    ) {
    }
}
