package com.cailu.bom.erp.interfaces;
import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.erp.application.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/erp/colors") @RequiredArgsConstructor
public class ColorController {
 private final ColorApplicationService service;
 @GetMapping public ResponseEntity<ApiResponse<List<ColorView>>> list(@RequestParam(required=false) String keyword){return ResponseEntity.ok(ApiResponse.success(service.list(keyword)));}
 @PostMapping public ResponseEntity<ApiResponse<ColorView>> create(@Valid @RequestBody ColorRequest r){return ResponseEntity.ok(ApiResponse.success(service.create(r.command())));}
 @PutMapping("/{id}") public ResponseEntity<ApiResponse<ColorView>> update(@PathVariable Long id,@Valid @RequestBody ColorRequest r){return ResponseEntity.ok(ApiResponse.success(service.update(id,r.command())));}
 @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.ok(ApiResponse.<Void>success(null));}
 public record ColorRequest(@NotBlank String code,@NotBlank String name,String shortName,String brandName,String seriesName,String manufacturerColorCode,String swatchImageUrl,Integer status,String remark){ColorCommand command(){return new ColorCommand(code,name,shortName,brandName,seriesName,manufacturerColorCode,swatchImageUrl,status,remark);}}
}