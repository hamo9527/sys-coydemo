package com.cailu.bom.finance.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fin_receivable")
public class FinReceivableDO extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String billNo;

    private Long customerId;

    private Long sourceOrderId;

    private BigDecimal amount;

    private BigDecimal receivedAmount;

    private LocalDate dueDate;

    /** open / partial / closed / void */
    private String status;
}
