package com.cailu.bom.erp.application;

import java.time.LocalDate;

public record CreateStockCountCommand(Long warehouseId, LocalDate billDate, String remark) {
    public CreateStockCountCommand {
        if (warehouseId == null || billDate == null) {
            throw new IllegalArgumentException("warehouse and bill date required");
        }
    }
}
