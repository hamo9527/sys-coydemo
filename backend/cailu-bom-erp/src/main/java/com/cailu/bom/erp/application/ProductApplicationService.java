package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

import java.util.List;

public interface ProductApplicationService {

    ProductView getById(Long id);

    PageResult<ProductView> list(String keyword, long page, long size);

    ProductView create(CreateProductCommand command);

    ProductImportResult importProducts(List<ProductImportRow> rows);
}
