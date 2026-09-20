package com.cailu.bom.finance.interfaces;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReceivePaymentRequest(
        @NotNull @DecimalMin("0.0001") BigDecimal amount
) {
}
