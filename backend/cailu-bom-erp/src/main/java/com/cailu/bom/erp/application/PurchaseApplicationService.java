package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

public interface PurchaseApplicationService {

    PurchaseView create(CreatePurchaseCommand command);

    PurchaseView getById(Long id);

    PageResult<PurchaseView> list(long page, long size);

    PurchaseView inbound(Long id);
}
