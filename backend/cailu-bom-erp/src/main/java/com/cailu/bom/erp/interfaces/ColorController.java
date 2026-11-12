package com.cailu.bom.erp.interfaces;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.erp.infrastructure.persistence.ErpColorDO;
import com.cailu.bom.erp.infrastructure.persistence.ErpColorMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/erp/colors")
@RequiredArgsConstructor
public class ColorController {
    private final ErpColorMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ErpColorDO>>> list(@RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<ErpColorDO> q = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) q.and(w -> w.like(ErpColorDO::getCode, keyword.trim())
                .or().like(ErpColorDO::getName, keyword.trim())
                .or().like(ErpColorDO::getBrandName, keyword.trim())
                .or().like(ErpColorDO::getManufacturerColorCode, keyword.trim()));
        q.orderByDesc(ErpColorDO::getId);
        return ResponseEntity.ok(ApiResponse.success(mapper.selectList(q)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ErpColorDO>> create(@Valid @RequestBody ColorRequest request) {
        ensureCodeAvailable(request.code(), null);
        ErpColorDO row = request.toDO();
        row.setId(IdWorker.getId());
        row.setStatus(request.status() == null ? 1 : request.status());
        mapper.insert(row);
        return ResponseEntity.ok(ApiResponse.success(row));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ErpColorDO>> update(@PathVariable Long id,
            @Valid @RequestBody ColorRequest request) {
        ErpColorDO old = mapper.selectById(id);
        if (old == null) throw new IllegalArgumentException("色号不存在");
        ensureCodeAvailable(request.code(), id);
        ErpColorDO row = request.toDO();
        row.setId(id);
        row.setStatus(request.status() == null ? old.getStatus() : request.status());
        mapper.updateById(row);
        return ResponseEntity.ok(ApiResponse.success(mapper.selectById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        mapper.deleteById(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null));
    }

    private void ensureCodeAvailable(String code, Long id) {
        LambdaQueryWrapper<ErpColorDO> q = new LambdaQueryWrapper<ErpColorDO>().eq(ErpColorDO::getCode, code);
        if (id != null) q.ne(ErpColorDO::getId, id);
        if (mapper.selectCount(q) > 0) throw new IllegalArgumentException("色号编码已存在");
    }

    public record ColorRequest(@NotBlank String code, @NotBlank String name, String shortName,
            String brandName, String seriesName, String manufacturerColorCode, String swatchImageUrl,
            Integer status, String remark) {
        ErpColorDO toDO() {
            ErpColorDO row = new ErpColorDO();
            row.setCode(code.trim()); row.setName(name.trim()); row.setShortName(shortName);
            row.setBrandName(brandName); row.setSeriesName(seriesName);
            row.setManufacturerColorCode(manufacturerColorCode); row.setSwatchImageUrl(swatchImageUrl);
            row.setRemark(remark);
            return row;
        }
    }
}
