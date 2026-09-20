package com.cailu.bom.oa.application;

public record ApprovalView(
        Long id,
        String title,
        String bizType,
        Long bizId,
        Long applicantId,
        String status,
        String remark
) {
}
