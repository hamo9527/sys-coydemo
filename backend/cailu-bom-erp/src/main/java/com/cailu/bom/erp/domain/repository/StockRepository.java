package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;

import java.math.BigDecimal;
import java.util.Optional;

public interface StockRepository {

    PageResult<Stock> page(Long warehouseId, Long productId, long page, long size);

    Optional<Stock> findByDimension(Long warehouseId, Long productId, String colorCode,
                                    String batchNo, String grade, String binName);

    Stock save(Stock stock);

    Stock increase(Long warehouseId, Long productId, String colorCode, String batchNo,
                   String grade, String binName, BigDecimal qty,
                   String bizType, Long bizId, String remark);

    Stock decrease(Long warehouseId, Long productId, String colorCode, String batchNo,
                   String grade, String binName, BigDecimal qty,
                   String bizType, Long bizId, String remark);
}
