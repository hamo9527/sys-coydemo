package com.cailu.bom.erp.domain.repository;
import com.cailu.bom.erp.domain.model.Color;
import java.util.List;
import java.util.Optional;
public interface ColorRepository { List<Color> findAll(String keyword); Optional<Color> findById(Long id); Optional<Color> findByCode(String code); Color save(Color color); void deleteById(Long id); }