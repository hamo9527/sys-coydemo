package com.cailu.bom.oa.application;

import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.oa.domain.model.Approval;
import com.cailu.bom.oa.domain.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalApplicationServiceImpl implements ApprovalApplicationService {

    private final ApprovalRepository approvalRepository;

    @Override
    public ApprovalView getById(Long id) {
        return toView(approvalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("approval not found", 404)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalView create(CreateApprovalCommand command) {
        Approval approval = new Approval(null, command.title(), command.bizType(),
                command.bizId(), command.applicantId(), "draft", command.remark());
        return toView(approvalRepository.save(approval));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalView submit(Long id) {
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("approval not found", 404));
        try {
            return toView(approvalRepository.save(approval.submit()));
        } catch (IllegalStateException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    private ApprovalView toView(Approval approval) {
        return new ApprovalView(approval.getId(), approval.getTitle(), approval.getBizType(),
                approval.getBizId(), approval.getApplicantId(), approval.getStatus(), approval.getRemark());
    }
}
