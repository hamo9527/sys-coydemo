package com.cailu.bom.erp.domain.repository;
import com.cailu.bom.erp.domain.model.Color;
import java.util.List;
public interface ProductColorBindingRepository { List<Color> findActiveColors(Long productId); void bind(Long productId,Long colorId); void unbind(Long productId,Long colorId); }