package com.cailu.bom.erp.application;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.infrastructure.persistence.ErpProductDO;
import com.cailu.bom.erp.infrastructure.persistence.ErpProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class ProductColorModeApplicationServiceImpl implements ProductColorModeApplicationService {
 private final ErpProductMapper mapper;
 public ProductColorModeView get(Long id) { ErpProductDO p=mapper.selectById(id); if(p==null) throw new BusinessException("product not found",404); return view(p); }
 @Transactional(rollbackFor=Exception.class) public ProductColorModeView update(Long id, ProductColorModeCommand c) { ErpProductDO p=mapper.selectById(id); if(p==null) throw new BusinessException("product not found",404); p.setColorManagementMode(c.colorManagementMode()); p.setBatchManagementEnabled(Boolean.TRUE.equals(c.batchManagementEnabled())); p.setGradeManagementEnabled(Boolean.TRUE.equals(c.gradeManagementEnabled())); mapper.updateById(p); return view(p); }
 private ProductColorModeView view(ErpProductDO p) { return new ProductColorModeView(p.getId(),p.getColorManagementMode(),p.getBatchManagementEnabled(),p.getGradeManagementEnabled()); }
}