package com.cailu.bom.oa.domain.model;

import lombok.Getter;

@Getter
public class Approval {

    private final Long id;
    private final String title;
    private final String bizType;
    private final Long bizId;
    private final Long applicantId;
    private final String status;
    private final String remark;

    public Approval(Long id, String title, String bizType, Long bizId,
                    Long applicantId, String status, String remark) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title required");
        }
        if (applicantId == null) {
            throw new IllegalArgumentException("applicantId required");
        }
        this.id = id;
        this.title = title;
        this.bizType = bizType;
        this.bizId = bizId;
        this.applicantId = applicantId;
        this.status = status == null ? "draft" : status;
        this.remark = remark;
    }

    public Approval submit() {
        if (!"draft".equals(status)) {
            throw new IllegalStateException("only draft can submit");
        }
        return new Approval(id, title, bizType, bizId, applicantId, "pending", remark);
    }
}
