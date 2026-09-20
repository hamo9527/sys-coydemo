package com.cailu.bom.oa.interfaces;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApprovalRequest(
        @NotBlank String title,
        String bizType,
        Long bizId,
        @NotNull Long applicantId,
        String remark
) {
}
