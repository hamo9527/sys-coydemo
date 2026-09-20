package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Product;
import com.cailu.bom.erp.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ErpProductMapper erpProductMapper;

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(erpProductMapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        ErpProductDO row = erpProductMapper.selectOne(new LambdaQueryWrapper<ErpProductDO>()
                .eq(ErpProductDO::getSku, sku)
                .last("LIMIT 1"));
        return Optional.ofNullable(row).map(this::toDomain);
    }

    @Override
    public PageResult<Product> page(String keyword, long page, long size) {
        long current = Math.max(page, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);
        LambdaQueryWrapper<ErpProductDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(ErpProductDO::getSku, keyword)
                    .or()
                    .like(ErpProductDO::getName, keyword));
        }
        wrapper.orderByDesc(ErpProductDO::getId);
        Page<ErpProductDO> result = erpProductMapper.selectPage(new Page<>(current, pageSize), wrapper);
        return new PageResult<>(
                result.getRecords().stream().map(this::toDomain).toList(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize());
    }

    @Override
    public Product save(Product product, String category, String spec) {
        ErpProductDO row = new ErpProductDO();
        row.setId(product.getId());
        row.setSku(product.getSku());
        row.setName(product.getName());
        row.setUnit(product.getUnit());
        row.setCategory(category);
        row.setSpec(spec);
        row.setPurchasePrice(product.getPurchasePrice());
        row.setSalePrice(product.getSalePrice());
        row.setStatus(product.getStatus());
        if (row.getId() == null) {
            erpProductMapper.insert(row);
        } else {
            erpProductMapper.updateById(row);
        }
        return toDomain(row);
    }

    private Product toDomain(ErpProductDO row) {
        return new Product(row.getId(), row.getSku(), row.getName(), row.getUnit(),
                row.getPurchasePrice(), row.getSalePrice(), row.getStatus());
    }
}
