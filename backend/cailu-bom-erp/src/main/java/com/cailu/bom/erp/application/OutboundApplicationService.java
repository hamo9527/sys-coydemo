package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;

public interface OutboundApplicationService {

    OutboundView create(CreateOutboundCommand command);

    OutboundView getById(Long id);

    PageResult<OutboundView> list(long page, long size);

    OutboundView confirm(Long id);
}
