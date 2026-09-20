package com.cailu.bom.oa.domain.repository;

import com.cailu.bom.oa.domain.model.Approval;

import java.util.Optional;

public interface ApprovalRepository {

    Optional<Approval> findById(Long id);

    Approval save(Approval approval);
}
