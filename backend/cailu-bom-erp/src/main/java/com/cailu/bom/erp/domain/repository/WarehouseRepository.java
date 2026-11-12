package com.cailu.bom.erp.domain.repository;

import com.cailu.bom.erp.domain.model.Warehouse;
import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {
    List<Warehouse> findAll(String keyword, Integer status);
    Optional<Warehouse> findById(Long id);
    Optional<Warehouse> findByCode(String code);
    Warehouse save(Warehouse warehouse);
    void deleteById(Long id);
}
