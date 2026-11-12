package com.cailu.bom.erp.infrastructure.persistence;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cailu.bom.erp.domain.model.ProductColorSettings;
import com.cailu.bom.erp.domain.repository.ProductColorModeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository @RequiredArgsConstructor
public class ProductColorModeRepositoryImpl implements ProductColorModeRepository {
 private final ErpProductMapper products; private final ErpStockMapper stocks;
 public Optional<ProductColorSettings> findByProductId(Long id){return Optional.ofNullable(products.selectById(id)).map(this::settings);}
 public ProductColorSettings save(ProductColorSettings s){ErpProductDO p=products.selectById(s.productId());if(p==null)throw new IllegalArgumentException("product not found");p.setColorManagementMode(s.colorManagementMode());p.setBatchManagementEnabled(s.batchManagementEnabled());p.setGradeManagementEnabled(s.gradeManagementEnabled());products.updateById(p);return settings(p);}
 public boolean hasStock(Long id){return stocks.selectCount(new LambdaQueryWrapper<ErpStockDO>().eq(ErpStockDO::getProductId,id))>0;}
 private ProductColorSettings settings(ErpProductDO p){return new ProductColorSettings(p.getId(),p.getColorManagementMode(),p.getBatchManagementEnabled(),p.getGradeManagementEnabled());}
}