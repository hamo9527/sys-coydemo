package com.cailu.bom.erp.interfaces;
import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.erp.application.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/erp/products/{productId}/color-management") @RequiredArgsConstructor
public class ProductColorModeController {
 private final ProductColorModeApplicationService service;
 @GetMapping public ResponseEntity<ApiResponse<ProductColorModeView>> get(@PathVariable Long productId) { return ResponseEntity.ok(ApiResponse.success(service.get(productId))); }
 @PutMapping public ResponseEntity<ApiResponse<ProductColorModeView>> update(@PathVariable Long productId,@RequestBody ProductColorModeRequest r) { return ResponseEntity.ok(ApiResponse.success(service.update(productId,new ProductColorModeCommand(r.colorManagementMode(),r.batchManagementEnabled(),r.gradeManagementEnabled())))); }
 public record ProductColorModeRequest(String colorManagementMode,Boolean batchManagementEnabled,Boolean gradeManagementEnabled) {}
}