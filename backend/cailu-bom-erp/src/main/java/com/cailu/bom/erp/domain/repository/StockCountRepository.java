package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.StockCount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockCountRepository {

    PageResult<StockCount> page(long page, long size);

    Optional<StockCount> findById(Long id);

    Optional<StockCount> findByIdForUpdate(Long id);

    Optional<StockCount> findByBillNo(String billNo);

    StockCount save(StockCount stockCount);

    void updateActualQuantities(Long countId, List<ActualQuantity> quantities);

    StockCount updateStatus(Long id, String status);

    record ActualQuantity(Long itemId, BigDecimal actualQty) {
    }
}
