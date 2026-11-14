package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.CreateStockCountCommand;
import com.cailu.bom.erp.application.StockCountApplicationService;
import com.cailu.bom.erp.application.StockCountView;
import com.cailu.bom.erp.application.UpdateStockCountCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/erp/stock-counts")
@RequiredArgsConstructor
public class StockCountController {

    private final StockCountApplicationService stockCountApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<StockCountView>>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(stockCountApplicationService.list(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockCountView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(stockCountApplicationService.get(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockCountView>> create(
            @Valid @RequestBody CreateStockCountRequest request) {
        CreateStockCountCommand command = new CreateStockCountCommand(
                request.warehouseId(), request.billDate(), request.remark());
        return ResponseEntity.ok(ApiResponse.success(stockCountApplicationService.create(command)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockCountView>> updateActualQuantities(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockCountRequest request) {
        UpdateStockCountCommand command = new UpdateStockCountCommand(request.items().stream()
                .map(item -> new UpdateStockCountCommand.Item(item.itemId(), item.actualQty()))
                .toList());
        return ResponseEntity.ok(ApiResponse.success(
                stockCountApplicationService.updateActualQuantities(id, command)));
    }

    @PostMapping("/{id}/confirmations")
    public ResponseEntity<ApiResponse<StockCountView>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(stockCountApplicationService.confirm(id)));
    }
}
