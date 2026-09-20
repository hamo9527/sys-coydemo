package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.Product;
import com.cailu.bom.erp.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductApplicationServiceImpl implements ProductApplicationService {

    private final ProductRepository productRepository;

    @Override
    public ProductView getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("product not found", 404));
        return toView(product);
    }

    @Override
    public PageResult<ProductView> list(String keyword, long page, long size) {
        PageResult<Product> result = productRepository.page(keyword, page, size);
        return new PageResult<>(
                result.records().stream().map(this::toView).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductView create(CreateProductCommand command) {
        productRepository.findBySku(command.sku()).ifPresent(p -> {
            throw new BusinessException("sku already exists");
        });
        Product product = new Product(null, command.sku(), command.name(), command.unit(),
                command.purchasePrice(), command.salePrice(), 1);
        Product saved = productRepository.save(product, command.category(), command.spec());
        return toView(saved);
    }

    private ProductView toView(Product product) {
        return new ProductView(product.getId(), product.getSku(), product.getName(),
                product.getUnit(), product.getPurchasePrice(), product.getSalePrice(), product.getStatus());
    }
}
