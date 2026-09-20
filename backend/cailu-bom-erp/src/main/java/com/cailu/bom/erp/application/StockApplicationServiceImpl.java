package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Stock;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockApplicationServiceImpl implements StockApplicationService {

    private final StockRepository stockRepository;

    @Override
    public PageResult<StockView> list(Long warehouseId, Long productId, long page, long size) {
        PageResult<Stock> result = stockRepository.page(warehouseId, productId, page, size);
        return new PageResult<>(
                result.records().stream().map(this::toView).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    private StockView toView(Stock stock) {
        return new StockView(stock.getId(), stock.getWarehouseId(), stock.getProductId(),
                stock.getQuantity(), stock.getLockedQty());
    }
}
