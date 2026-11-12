package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cailu.bom.erp.domain.model.Warehouse;
import com.cailu.bom.erp.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {
    private final ErpWarehouseMapper mapper;

    public List<Warehouse> findAll(String keyword, Integer status) {
        LambdaQueryWrapper<ErpWarehouseDO> q = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) q.and(w -> w.like(ErpWarehouseDO::getCode, keyword.trim())
                .or().like(ErpWarehouseDO::getName, keyword.trim()));
        if (status != null) q.eq(ErpWarehouseDO::getStatus, status);
        q.orderByDesc(ErpWarehouseDO::getId);
        return mapper.selectList(q).stream().map(this::toDomain).toList();
    }

    public Optional<Warehouse> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id)).map(this::toDomain);
    }

    public Optional<Warehouse> findByCode(String code) {
        return Optional.ofNullable(mapper.selectOne(new LambdaQueryWrapper<ErpWarehouseDO>()
                .eq(ErpWarehouseDO::getCode, code).last("LIMIT 1"))).map(this::toDomain);
    }

    public Warehouse save(Warehouse value) {
        ErpWarehouseDO row = new ErpWarehouseDO();
        row.setId(value.id());
        row.setCode(value.code());
        row.setName(value.name());
        row.setAddress(value.address());
        row.setStatus(value.status());
        if (row.getId() == null) mapper.insert(row); else mapper.updateById(row);
        return toDomain(row);
    }

    public void deleteById(Long id) { mapper.deleteById(id); }

    private Warehouse toDomain(ErpWarehouseDO row) {
        return new Warehouse(row.getId(), row.getCode(), row.getName(), row.getAddress(), row.getStatus());
    }
}
