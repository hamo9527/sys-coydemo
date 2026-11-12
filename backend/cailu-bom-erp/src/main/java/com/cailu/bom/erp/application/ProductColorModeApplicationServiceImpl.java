package com.cailu.bom.erp.application;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.ProductColorSettings;
import com.cailu.bom.erp.domain.repository.ProductColorModeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
@Service @RequiredArgsConstructor
public class ProductColorModeApplicationServiceImpl implements ProductColorModeApplicationService {
 private final ProductColorModeRepository repository;
 public ProductColorModeView get(Long id){return repository.findByProductId(id).map(this::view).orElseThrow(()->new BusinessException("product not found",404));}
 @Transactional(rollbackFor=Exception.class) public ProductColorModeView update(Long id,ProductColorModeCommand c){ProductColorSettings old=repository.findByProductId(id).orElseThrow(()->new BusinessException("product not found",404));if(repository.hasStock(id) && (!Objects.equals(c.colorManagementMode(),old.colorManagementMode()) || !Objects.equals(Boolean.TRUE.equals(c.batchManagementEnabled()),old.batchManagementEnabled()) || !Objects.equals(Boolean.TRUE.equals(c.gradeManagementEnabled()),old.gradeManagementEnabled()))) throw new BusinessException("商品已有库存，不能直接修改色号、批次或等级管理方式；请先完成库存维度转换");return view(repository.save(new ProductColorSettings(id,c.colorManagementMode(),Boolean.TRUE.equals(c.batchManagementEnabled()),Boolean.TRUE.equals(c.gradeManagementEnabled()))));}
 private ProductColorModeView view(ProductColorSettings s){return new ProductColorModeView(s.productId(),s.colorManagementMode(),s.batchManagementEnabled(),s.gradeManagementEnabled());}
}