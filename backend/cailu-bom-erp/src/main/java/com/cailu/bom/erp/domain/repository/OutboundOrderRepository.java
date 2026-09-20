package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.OutboundOrder;

import java.util.Optional;

public interface OutboundOrderRepository {

    Optional<OutboundOrder> findById(Long id);

    Optional<OutboundOrder> findByBillNo(String billNo);

    PageResult<OutboundOrder> page(long page, long size);

    OutboundOrder save(OutboundOrder order);

    OutboundOrder updateStatus(Long id, String status);
}
