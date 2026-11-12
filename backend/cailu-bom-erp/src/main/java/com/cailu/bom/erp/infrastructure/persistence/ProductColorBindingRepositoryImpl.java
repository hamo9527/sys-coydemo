package com.cailu.bom.erp.infrastructure.persistence;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cailu.bom.erp.domain.model.Color;
import com.cailu.bom.erp.domain.repository.ProductColorBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository @RequiredArgsConstructor
public class ProductColorBindingRepositoryImpl implements ProductColorBindingRepository {
 private final ErpProductColorMapper bindings; private final ErpColorMapper colors;
 public List<Color> findActiveColors(Long productId){List<Long> ids=bindings.selectList(new LambdaQueryWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getStatus,1)).stream().map(ErpProductColorDO::getColorId).toList();if(ids.isEmpty())return List.of();return colors.selectList(new LambdaQueryWrapper<ErpColorDO>().in(ErpColorDO::getId,ids).eq(ErpColorDO::getStatus,1)).stream().map(this::domain).toList();}
 public void bind(Long productId,Long colorId){ErpProductColorDO old=bindings.selectOne(new LambdaQueryWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getColorId,colorId));if(old==null){ErpProductColorDO row=new ErpProductColorDO();row.setId(IdWorker.getId());row.setProductId(productId);row.setColorId(colorId);row.setAllowInbound(true);row.setAllowOutbound(true);row.setStatus(1);bindings.insert(row);}else{bindings.update(null,new LambdaUpdateWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getId,old.getId()).set(ErpProductColorDO::getStatus,1));}}
 public void unbind(Long productId,Long colorId){bindings.update(null,new LambdaUpdateWrapper<ErpProductColorDO>().eq(ErpProductColorDO::getProductId,productId).eq(ErpProductColorDO::getColorId,colorId).set(ErpProductColorDO::getStatus,0));}
 private Color domain(ErpColorDO d){return new Color(d.getId(),d.getCode(),d.getName(),d.getShortName(),d.getBrandName(),d.getSeriesName(),d.getManufacturerColorCode(),d.getSwatchImageUrl(),d.getStatus(),d.getRemark());}
}