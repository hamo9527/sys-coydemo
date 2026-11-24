package com.cailu.bom.erp.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record UpdateStockCountRequest(@NotNull @Valid List<Item> items) {
    public record Item(
            @NotNull Long productId,
            String colorCode,
            String batchNo,
            String grade,
            String binName,
            @NotNull BigDecimal costPrice,
            BigDecimal actualQty
    ) {
    }
}
