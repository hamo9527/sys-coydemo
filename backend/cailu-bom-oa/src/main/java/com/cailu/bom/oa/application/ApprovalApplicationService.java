package com.cailu.bom.oa.application;

public interface ApprovalApplicationService {

    ApprovalView getById(Long id);

    ApprovalView create(CreateApprovalCommand command);

    ApprovalView submit(Long id);
}
