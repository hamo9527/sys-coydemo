package com.cailu.bom.finance.interfaces;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReceivableRequest(
        @NotBlank String billNo,
        @NotNull Long customerId,
        Long sourceOrderId,
        @NotNull @DecimalMin("0.0001") BigDecimal amount,
        LocalDate dueDate
) {
}
