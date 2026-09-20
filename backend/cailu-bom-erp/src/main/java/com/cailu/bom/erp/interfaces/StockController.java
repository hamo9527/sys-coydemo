package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.StockApplicationService;
import com.cailu.bom.erp.application.StockView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/erp/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockApplicationService stockApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<StockView>>> list(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(
                stockApplicationService.list(warehouseId, productId, page, size)));
    }
}
