package com.cailu.bom.oa.application;

public record CreateApprovalCommand(
        String title,
        String bizType,
        Long bizId,
        Long applicantId,
        String remark
) {
    public CreateApprovalCommand {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title required");
        }
        if (applicantId == null) {
            throw new IllegalArgumentException("applicantId required");
        }
    }
}
