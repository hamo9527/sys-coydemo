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
        Product product = Product.builder()
                .sku(command.sku())
                .mnemonicCode(command.mnemonicCode())
                .name(command.name())
                .brandName(command.brandName())
                .category(command.category())
                .spec(command.spec())
                .unit(command.unit())
                .auxiliaryUnit(command.auxiliaryUnit())
                .auxiliarySpec(command.auxiliarySpec())
                .packageUnit(command.packageUnit())
                .packageSpec(command.packageSpec())
                .unitWeight(command.unitWeight())
                .minOrderQty(command.minOrderQty())
                .minStock(command.minStock())
                .maxStock(command.maxStock())
                .smallColorStock(command.smallColorStock())
                .stockAgeThreshold(command.stockAgeThreshold())
                .slowMovingDays(command.slowMovingDays())
                .shelfLifeDays(command.shelfLifeDays())
                .productAttribute(command.productAttribute())
                .virtualProduct(Boolean.TRUE.equals(command.virtualProduct()))
                .installationIncluded(Boolean.TRUE.equals(command.installationIncluded()))
                .cloudStoreVisible(Boolean.TRUE.equals(command.cloudStoreVisible()))
                .productionProcess(command.productionProcess())
                .productionOrigin(command.productionOrigin())
                .usageLocation(command.usageLocation())
                .vrUrl(command.vrUrl())
                .remark(command.remark())
                .purchasePrice(command.purchasePrice())
                .salePrice(command.salePrice())
                .status(1)
                .build();
        return toView(productRepository.save(product));
    }

    private ProductView toView(Product product) {
        return new ProductView(
                product.getId(),
                product.getSku(),
                product.getMnemonicCode(),
                product.getName(),
                product.getBrandName(),
                product.getCategory(),
                product.getSpec(),
                product.getUnit(),
                product.getAuxiliaryUnit(),
                product.getAuxiliarySpec(),
                product.getPackageUnit(),
                product.getPackageSpec(),
                product.getUnitWeight(),
                product.getMinOrderQty(),
                product.getMinStock(),
                product.getMaxStock(),
                product.getSmallColorStock(),
                product.getStockAgeThreshold(),
                product.getSlowMovingDays(),
                product.getShelfLifeDays(),
                product.getProductAttribute(),
                product.getVirtualProduct(),
                product.getInstallationIncluded(),
                product.getCloudStoreVisible(),
                product.getProductionProcess(),
                product.getProductionOrigin(),
                product.getUsageLocation(),
                product.getVrUrl(),
                product.getRemark(),
                product.getPurchasePrice(),
                product.getSalePrice(),
                product.getStatus());
    }
}
