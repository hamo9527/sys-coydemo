package com.cailu.bom.erp.application;

import java.util.List;

public interface WarehouseApplicationService {
    List<WarehouseView> list(String keyword, Integer status);
    WarehouseView get(Long id);
    WarehouseView create(WarehouseCommand command);
    WarehouseView update(Long id, WarehouseCommand command);
    void delete(Long id);
}
