package com.cailu.bom.finance.application;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReceivableCommand(
        String billNo,
        Long customerId,
        Long sourceOrderId,
        BigDecimal amount,
        LocalDate dueDate
) {
    public CreateReceivableCommand {
        if (billNo == null || billNo.isBlank()) {
            throw new IllegalArgumentException("billNo required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }
}
