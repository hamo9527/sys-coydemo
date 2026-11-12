package com.cailu.bom.erp.application;
import com.cailu.bom.erp.infrastructure.persistence.ErpColorDO;
import java.util.List;
public interface ProductColorBindingApplicationService { List<ErpColorDO> list(Long productId); void bind(Long productId,Long colorId); void unbind(Long productId,Long colorId); }