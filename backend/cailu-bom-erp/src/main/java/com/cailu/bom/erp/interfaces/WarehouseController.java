package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.erp.application.WarehouseApplicationService;
import com.cailu.bom.erp.application.WarehouseCommand;
import com.cailu.bom.erp.application.WarehouseView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/erp/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseApplicationService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseView>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return ResponseEntity.ok(ApiResponse.success(service.list(keyword, status)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.get(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseView>> create(@Valid @RequestBody WarehouseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.create(request.command())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseView>> update(@PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, request.command())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null));
    }

    public record WarehouseRequest(@NotBlank String code, @NotBlank String name,
                                   String address, Integer status) {
        WarehouseCommand command() { return new WarehouseCommand(code, name, address, status); }
    }
}
