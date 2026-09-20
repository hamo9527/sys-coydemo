package com.cailu.bom.erp.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreatePurchaseRequest(
        @NotBlank String orderNo,
        @NotNull Long supplierId,
        @NotNull Long warehouseId,
        String remark,
        @NotEmpty @Valid List<Item> items
) {
    public record Item(
            @NotNull Long productId,
            @NotNull BigDecimal qty,
            @NotNull BigDecimal price
    ) {
    }
}
