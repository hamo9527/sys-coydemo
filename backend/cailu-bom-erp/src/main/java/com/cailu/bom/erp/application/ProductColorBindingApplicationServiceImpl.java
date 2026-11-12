package com.cailu.bom.erp.application;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.repository.ColorRepository;
import com.cailu.bom.erp.domain.repository.ProductColorBindingRepository;
import com.cailu.bom.erp.domain.repository.ProductColorModeRepository;
import com.cailu.bom.erp.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ProductColorBindingApplicationServiceImpl implements ProductColorBindingApplicationService {
 private final ProductRepository products;private final ColorRepository colors;private final ProductColorModeRepository modes;private final ProductColorBindingRepository bindings;
 public List<com.cailu.bom.erp.infrastructure.persistence.ErpColorDO> list(Long id){if(products.findById(id).isEmpty())throw new BusinessException("product not found",404);return bindings.findActiveColors(id).stream().map(c->{com.cailu.bom.erp.infrastructure.persistence.ErpColorDO d=new com.cailu.bom.erp.infrastructure.persistence.ErpColorDO();d.setId(c.id());d.setCode(c.code());d.setName(c.name());d.setShortName(c.shortName());d.setBrandName(c.brandName());d.setSeriesName(c.seriesName());d.setManufacturerColorCode(c.manufacturerColorCode());d.setSwatchImageUrl(c.swatchImageUrl());d.setStatus(c.status());d.setRemark(c.remark());return d;}).toList();}
 @Transactional(rollbackFor=Exception.class) public void bind(Long productId,Long colorId){if(products.findById(productId).isEmpty())throw new BusinessException("product not found",404);if(colors.findById(colorId).isEmpty())throw new BusinessException("color not found",404);String mode=modes.findByProductId(productId).orElseThrow(()->new BusinessException("product not found",404)).colorManagementMode();if(!"BOUND".equals(mode))throw new BusinessException("只有档案绑定色号模式允许维护绑定关系");bindings.bind(productId,colorId);}
 @Transactional(rollbackFor=Exception.class) public void unbind(Long productId,Long colorId){if(products.findById(productId).isEmpty())throw new BusinessException("product not found",404);bindings.unbind(productId,colorId);}
}