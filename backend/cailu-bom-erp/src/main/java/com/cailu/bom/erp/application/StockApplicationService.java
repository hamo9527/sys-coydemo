package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

import java.util.List;

public interface StockApplicationService {

    PageResult<StockView> list(Long warehouseId, Long productId, long page, long size);

    List<String> suggestColorCodes(Long productId, String keyword);
}
