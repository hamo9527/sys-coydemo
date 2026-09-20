package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    PageResult<Product> page(String keyword, long page, long size);

    Product save(Product product, String category, String spec);
}
