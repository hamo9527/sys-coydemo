package com.cailu.bom.erp.application;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ProductColorBindingApplicationServiceImpl implements ProductColorBindingApplicationService {
 private final ErpProductMapper products; private final ErpColorMapper colors; private final ErpProductColorMapper bindings;
 public List<ErpColorDO> list(Long productId) { requireProduct(productId); List<Long> ids=bindings.selectList(new LambdaQueryWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getStatus,1)).stream().map(ErpProductColorDO::getColorId).toList(); return ids.isEmpty()?List.of():colors.selectBatchIds(ids); }
 @Transactional(rollbackFor=Exception.class) public void bind(Long productId,Long colorId) { ErpProductDO p=requireProduct(productId); if(!"BOUND".equals(p.getColorManagementMode())) throw new BusinessException("只有档案绑定色号模式允许维护绑定关系"); if(colors.selectById(colorId)==null) throw new BusinessException("color not found",404); ErpProductColorDO old=bindings.selectOne(new LambdaQueryWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getColorId,colorId)); if(old==null) { ErpProductColorDO row=new ErpProductColorDO(); row.setId(IdWorker.getId()); row.setProductId(productId); row.setColorId(colorId); row.setAllowInbound(true); row.setAllowOutbound(true); row.setStatus(1); bindings.insert(row); } else { bindings.update(null,new LambdaUpdateWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getId,old.getId()).set(ErpProductColorDO::getStatus,1)); } }
 @Transactional(rollbackFor=Exception.class) public void unbind(Long productId,Long colorId) { requireProduct(productId); bindings.update(null,new LambdaUpdateWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getColorId,colorId).set(ErpProductColorDO::getStatus,0)); }
 private ErpProductDO requireProduct(Long id) { ErpProductDO p=products.selectById(id); if(p==null) throw new BusinessException("product not found",404); return p; }
}