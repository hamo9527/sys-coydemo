package com.cailu.bom.erp.application;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.ProductColorSettings;
import com.cailu.bom.erp.domain.repository.ProductColorModeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class ProductColorModeApplicationServiceImpl implements ProductColorModeApplicationService {
 private final ProductColorModeRepository repository;
 public ProductColorModeView get(Long id){return repository.findByProductId(id).map(this::view).orElseThrow(()->new BusinessException("product not found",404));}
 @Transactional(rollbackFor=Exception.class) public ProductColorModeView update(Long id,ProductColorModeCommand c){ProductColorSettings old=repository.findByProductId(id).orElseThrow(()->new BusinessException("product not found",404));if(!c.colorManagementMode().equals(old.colorManagementMode())&&repository.hasStock(id))throw new BusinessException("商品已有库存，不能直接切换色号管理方式；请先完成库存维度转换");return view(repository.save(new ProductColorSettings(id,c.colorManagementMode(),Boolean.TRUE.equals(c.batchManagementEnabled()),Boolean.TRUE.equals(c.gradeManagementEnabled()))));}
 private ProductColorModeView view(ProductColorSettings s){return new ProductColorModeView(s.productId(),s.colorManagementMode(),s.batchManagementEnabled(),s.gradeManagementEnabled());}
}