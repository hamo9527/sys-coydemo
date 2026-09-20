package com.cailu.bom.finance.application;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceivableView(
        Long id,
        String billNo,
        Long customerId,
        Long sourceOrderId,
        BigDecimal amount,
        BigDecimal receivedAmount,
        LocalDate dueDate,
        String status
) {
}
