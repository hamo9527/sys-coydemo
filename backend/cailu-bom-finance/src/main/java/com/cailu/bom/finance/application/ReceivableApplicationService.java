package com.cailu.bom.finance.application;

import java.math.BigDecimal;

public interface ReceivableApplicationService {

    ReceivableView getById(Long id);

    ReceivableView create(CreateReceivableCommand command);

    ReceivableView receive(Long id, BigDecimal payment);
}
