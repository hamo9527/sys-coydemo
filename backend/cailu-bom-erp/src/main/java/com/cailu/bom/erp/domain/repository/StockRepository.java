package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;

import java.math.BigDecimal;
import java.util.Optional;

public interface StockRepository {

    PageResult<Stock> page(Long warehouseId, Long productId, long page, long size);

    Optional<Stock> findByWarehouseAndProduct(Long warehouseId, Long productId);

    Stock save(Stock stock);

    /**
     * Upsert stock by warehouse+product, add qty, and append a ledger row.
     */
    Stock increase(Long warehouseId, Long productId, BigDecimal qty,
                   String bizType, Long bizId, String remark);

    /**
     * Decrease stock by warehouse+product and append a ledger row (negative change).
     */
    Stock decrease(Long warehouseId, Long productId, BigDecimal qty,
                   String bizType, Long bizId, String remark);
}
