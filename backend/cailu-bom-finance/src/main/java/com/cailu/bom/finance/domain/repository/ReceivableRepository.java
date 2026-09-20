package com.cailu.bom.finance.domain.repository;

import com.cailu.bom.finance.domain.model.Receivable;

import java.util.Optional;

public interface ReceivableRepository {

    Optional<Receivable> findById(Long id);

    Optional<Receivable> findByBillNo(String billNo);

    Receivable save(Receivable receivable);
}
