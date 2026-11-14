package com.cailu.bom.erp.interfaces;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateStockCountRequest(
        @NotNull Long warehouseId,
        @NotNull LocalDate billDate,
        String remark
) {
}
