package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

public interface StockCountApplicationService {

    PageResult<StockCountView> list(long page, long size);

    StockCountView get(Long id);

    StockCountView create(CreateStockCountCommand command);

    StockCountView updateActualQuantities(Long id, UpdateStockCountCommand command);

    StockCountView confirm(Long id);
}
