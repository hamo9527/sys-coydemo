package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

public interface StockApplicationService {

    PageResult<StockView> list(Long warehouseId, Long productId, long page, long size);
}
