package com.cailu.bom.erp.infrastructure.persistence;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cailu.bom.erp.domain.model.Color;
import com.cailu.bom.erp.domain.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository @RequiredArgsConstructor
public class ColorRepositoryImpl implements ColorRepository {
 private final ErpColorMapper mapper;
 public List<Color> findAll(String keyword){ LambdaQueryWrapper<ErpColorDO> q=new LambdaQueryWrapper<>(); if(keyword!=null&&!keyword.isBlank()) q.and(w->w.like(ErpColorDO::getCode,keyword.trim()).or().like(ErpColorDO::getName,keyword.trim()).or().like(ErpColorDO::getBrandName,keyword.trim()).or().like(ErpColorDO::getManufacturerColorCode,keyword.trim())); q.orderByDesc(ErpColorDO::getId); return mapper.selectList(q).stream().map(this::domain).toList(); }
 public Optional<Color> findById(Long id){return Optional.ofNullable(mapper.selectById(id)).map(this::domain);}
 public Optional<Color> findByCode(String code){return Optional.ofNullable(mapper.selectOne(new LambdaQueryWrapper<ErpColorDO>().eq(ErpColorDO::getCode,code).last("LIMIT 1"))).map(this::domain);}
 public Color save(Color c){ErpColorDO d=new ErpColorDO();d.setId(c.id());d.setCode(c.code());d.setName(c.name());d.setShortName(c.shortName());d.setBrandName(c.brandName());d.setSeriesName(c.seriesName());d.setManufacturerColorCode(c.manufacturerColorCode());d.setSwatchImageUrl(c.swatchImageUrl());d.setStatus(c.status());d.setRemark(c.remark());if(d.getId()==null)mapper.insert(d);else mapper.updateById(d);return domain(d);}
 public void deleteById(Long id){mapper.deleteById(id);}
 private Color domain(ErpColorDO d){return new Color(d.getId(),d.getCode(),d.getName(),d.getShortName(),d.getBrandName(),d.getSeriesName(),d.getManufacturerColorCode(),d.getSwatchImageUrl(),d.getStatus(),d.getRemark());}
}