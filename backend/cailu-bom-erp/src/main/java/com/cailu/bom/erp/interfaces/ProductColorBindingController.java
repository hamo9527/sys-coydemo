package com.cailu.bom.erp.interfaces;
import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.erp.application.ProductColorBindingApplicationService;
import com.cailu.bom.erp.infrastructure.persistence.ErpColorDO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/erp/products/{productId}/colors") @RequiredArgsConstructor
public class ProductColorBindingController {
 private final ProductColorBindingApplicationService service;
 @GetMapping public ResponseEntity<ApiResponse<List<ErpColorDO>>> list(@PathVariable Long productId) { return ResponseEntity.ok(ApiResponse.success(service.list(productId))); }
 @PostMapping("/{colorId}") public ResponseEntity<ApiResponse<Void>> bind(@PathVariable Long productId,@PathVariable Long colorId) { service.bind(productId,colorId); return ResponseEntity.ok(ApiResponse.<Void>success(null)); }
 @DeleteMapping("/{colorId}") public ResponseEntity<ApiResponse<Void>> unbind(@PathVariable Long productId,@PathVariable Long colorId) { service.unbind(productId,colorId); return ResponseEntity.ok(ApiResponse.<Void>success(null)); }
}