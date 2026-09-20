package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.CreateProductCommand;
import com.cailu.bom.erp.application.ProductApplicationService;
import com.cailu.bom.erp.application.ProductView;
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
@RequestMapping("/api/erp/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplicationService productApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ProductView>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.list(keyword, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductView>> create(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(
                request.sku(), request.name(), request.category(), request.unit(),
                request.spec(), request.purchasePrice(), request.salePrice());
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.create(command)));
    }
}
