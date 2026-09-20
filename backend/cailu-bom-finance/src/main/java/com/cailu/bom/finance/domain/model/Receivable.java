package com.cailu.bom.finance.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Receivable {

    private final Long id;
    private final String billNo;
    private final Long customerId;
    private final Long sourceOrderId;
    private final BigDecimal amount;
    private final BigDecimal receivedAmount;
    private final LocalDate dueDate;
    private final String status;

    public Receivable(Long id, String billNo, Long customerId, Long sourceOrderId,
                      BigDecimal amount, BigDecimal receivedAmount, LocalDate dueDate, String status) {
        if (billNo == null || billNo.isBlank()) {
            throw new IllegalArgumentException("billNo required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.id = id;
        this.billNo = billNo;
        this.customerId = customerId;
        this.sourceOrderId = sourceOrderId;
        this.amount = amount;
        this.receivedAmount = receivedAmount == null ? BigDecimal.ZERO : receivedAmount;
        this.dueDate = dueDate;
        this.status = status == null ? "open" : status;
    }

    public Receivable receive(BigDecimal payment) {
        if (payment == null || payment.signum() <= 0) {
            throw new IllegalArgumentException("payment must be positive");
        }
        BigDecimal next = receivedAmount.add(payment);
        if (next.compareTo(amount) > 0) {
            throw new IllegalArgumentException("overpayment not allowed");
        }
        String nextStatus = next.compareTo(amount) == 0 ? "closed" : "partial";
        return new Receivable(id, billNo, customerId, sourceOrderId, amount, next, dueDate, nextStatus);
    }
}
