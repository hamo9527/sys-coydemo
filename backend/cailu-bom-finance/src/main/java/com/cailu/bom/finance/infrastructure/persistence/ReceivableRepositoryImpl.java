package com.cailu.bom.finance.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cailu.bom.finance.domain.model.Receivable;
import com.cailu.bom.finance.domain.repository.ReceivableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReceivableRepositoryImpl implements ReceivableRepository {

    private final FinReceivableMapper finReceivableMapper;

    @Override
    public Optional<Receivable> findById(Long id) {
        return Optional.ofNullable(finReceivableMapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public Optional<Receivable> findByBillNo(String billNo) {
        FinReceivableDO row = finReceivableMapper.selectOne(new LambdaQueryWrapper<FinReceivableDO>()
                .eq(FinReceivableDO::getBillNo, billNo)
                .last("LIMIT 1"));
        return Optional.ofNullable(row).map(this::toDomain);
    }

    @Override
    public Receivable save(Receivable receivable) {
        FinReceivableDO row = new FinReceivableDO();
        row.setId(receivable.getId());
        row.setBillNo(receivable.getBillNo());
        row.setCustomerId(receivable.getCustomerId());
        row.setSourceOrderId(receivable.getSourceOrderId());
        row.setAmount(receivable.getAmount());
        row.setReceivedAmount(receivable.getReceivedAmount());
        row.setDueDate(receivable.getDueDate());
        row.setStatus(receivable.getStatus());
        if (row.getId() == null) {
            finReceivableMapper.insert(row);
        } else {
            finReceivableMapper.updateById(row);
        }
        return toDomain(row);
    }

    private Receivable toDomain(FinReceivableDO row) {
        return new Receivable(row.getId(), row.getBillNo(), row.getCustomerId(), row.getSourceOrderId(),
                row.getAmount(), row.getReceivedAmount(), row.getDueDate(), row.getStatus());
    }
}
