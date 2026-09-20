package com.cailu.bom.oa.infrastructure.persistence;

import com.cailu.bom.oa.domain.model.Approval;
import com.cailu.bom.oa.domain.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApprovalRepositoryImpl implements ApprovalRepository {

    private final OaApprovalMapper oaApprovalMapper;

    @Override
    public Optional<Approval> findById(Long id) {
        return Optional.ofNullable(oaApprovalMapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public Approval save(Approval approval) {
        OaApprovalDO row = new OaApprovalDO();
        row.setId(approval.getId());
        row.setTitle(approval.getTitle());
        row.setBizType(approval.getBizType());
        row.setBizId(approval.getBizId());
        row.setApplicantId(approval.getApplicantId());
        row.setStatus(approval.getStatus());
        row.setRemark(approval.getRemark());
        if (row.getId() == null) {
            oaApprovalMapper.insert(row);
        } else {
            oaApprovalMapper.updateById(row);
        }
        return toDomain(row);
    }

    private Approval toDomain(OaApprovalDO row) {
        return new Approval(row.getId(), row.getTitle(), row.getBizType(), row.getBizId(),
                row.getApplicantId(), row.getStatus(), row.getRemark());
    }
}
