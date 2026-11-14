package com.cailu.bom.erp.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record UpdateStockCountRequest(@NotEmpty @Valid List<Item> items) {
    public record Item(@NotNull Long itemId, @NotNull BigDecimal actualQty) {
    }
}
