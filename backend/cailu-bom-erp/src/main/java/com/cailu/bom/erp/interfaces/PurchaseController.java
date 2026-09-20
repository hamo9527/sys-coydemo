package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.CreatePurchaseCommand;
import com.cailu.bom.erp.application.PurchaseApplicationService;
import com.cailu.bom.erp.application.PurchaseView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/erp/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseApplicationService purchaseApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseView>> create(@Valid @RequestBody CreatePurchaseRequest request) {
        CreatePurchaseCommand command = new CreatePurchaseCommand(
                request.orderNo(),
                request.supplierId(),
                request.warehouseId(),
                request.remark(),
                request.items().stream()
                        .map(i -> new CreatePurchaseCommand.Item(i.productId(), i.qty(), i.price()))
                        .toList());
        return ResponseEntity.ok(ApiResponse.success(purchaseApplicationService.create(command)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseApplicationService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<PurchaseView>>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(purchaseApplicationService.list(page, size)));
    }

    @PostMapping("/{id}/inbounds")
    public ResponseEntity<ApiResponse<PurchaseView>> inbound(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseApplicationService.inbound(id)));
    }
}
