package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRepository {

    PageResult<Stock> page(Long warehouseId, Long productId, long page, long size);

    Optional<Stock> findByDimension(Long warehouseId, Long productId, String colorCode,
                                    String batchNo, String grade, String binName);

    List<String> suggestColorCodes(Long productId, String keyword);

    List<Stock> balancesAtDate(Long warehouseId, LocalDate billDate);

    Stock save(Stock stock);

    Stock increase(Long warehouseId, Long productId, String colorCode, String batchNo,
                   String grade, String binName, BigDecimal qty,
                   String bizType, Long bizId, LocalDate bizDate, String remark);

    Stock decrease(Long warehouseId, Long productId, String colorCode, String batchNo,
                   String grade, String binName, BigDecimal qty,
                   String bizType, Long bizId, String remark);
}
