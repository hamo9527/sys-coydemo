package com.cailu.bom.erp.application;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.Color;
import com.cailu.bom.erp.domain.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ColorApplicationServiceImpl implements ColorApplicationService {
 private final ColorRepository repository;
 public List<ColorView> list(String keyword){return repository.findAll(keyword).stream().map(this::view).toList();}
 @Transactional(rollbackFor=Exception.class) public ColorView create(ColorCommand c){if(repository.findByCode(c.code().trim()).isPresent())throw new BusinessException("色号编码已存在");return view(repository.save(color(null,c)));}
 @Transactional(rollbackFor=Exception.class) public ColorView update(Long id,ColorCommand c){Color old=repository.findById(id).orElseThrow(()->new BusinessException("color not found",404));repository.findByCode(c.code().trim()).filter(x->!x.id().equals(id)).ifPresent(x->{throw new BusinessException("色号编码已存在");});return view(repository.save(color(id,c.status()==null?new ColorCommand(c.code(),c.name(),c.shortName(),c.brandName(),c.seriesName(),c.manufacturerColorCode(),c.swatchImageUrl(),old.status(),c.remark()):c)));}
 @Transactional(rollbackFor=Exception.class) public void delete(Long id){repository.findById(id).orElseThrow(()->new BusinessException("color not found",404));repository.deleteById(id);}
 private Color color(Long id,ColorCommand c){return new Color(id,c.code().trim(),c.name().trim(),c.shortName(),c.brandName(),c.seriesName(),c.manufacturerColorCode(),c.swatchImageUrl(),c.status()==null?1:c.status(),c.remark());}
 private ColorView view(Color c){return new ColorView(c.id(),c.code(),c.name(),c.shortName(),c.brandName(),c.seriesName(),c.manufacturerColorCode(),c.swatchImageUrl(),c.status(),c.remark());}
}