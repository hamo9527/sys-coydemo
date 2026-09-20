package com.cailu.bom.finance.application;

import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.finance.domain.model.Receivable;
import com.cailu.bom.finance.domain.repository.ReceivableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReceivableApplicationServiceImpl implements ReceivableApplicationService {

    private final ReceivableRepository receivableRepository;

    @Override
    public ReceivableView getById(Long id) {
        return toView(receivableRepository.findById(id)
                .orElseThrow(() -> new BusinessException("receivable not found", 404)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceivableView create(CreateReceivableCommand command) {
        receivableRepository.findByBillNo(command.billNo()).ifPresent(r -> {
            throw new BusinessException("billNo already exists");
        });
        Receivable receivable = new Receivable(null, command.billNo(), command.customerId(),
                command.sourceOrderId(), command.amount(), BigDecimal.ZERO, command.dueDate(), "open");
        return toView(receivableRepository.save(receivable));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceivableView receive(Long id, BigDecimal payment) {
        Receivable receivable = receivableRepository.findById(id)
                .orElseThrow(() -> new BusinessException("receivable not found", 404));
        try {
            return toView(receivableRepository.save(receivable.receive(payment)));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    private ReceivableView toView(Receivable receivable) {
        return new ReceivableView(receivable.getId(), receivable.getBillNo(), receivable.getCustomerId(),
                receivable.getSourceOrderId(), receivable.getAmount(), receivable.getReceivedAmount(),
                receivable.getDueDate(), receivable.getStatus());
    }
}
