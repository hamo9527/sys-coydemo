package com.cailu.bom.erp.domain.repository;
import com.cailu.bom.erp.domain.model.ProductColorSettings;
import java.util.Optional;
public interface ProductColorModeRepository { Optional<ProductColorSettings> findByProductId(Long productId); ProductColorSettings save(ProductColorSettings settings); boolean hasStock(Long productId); }