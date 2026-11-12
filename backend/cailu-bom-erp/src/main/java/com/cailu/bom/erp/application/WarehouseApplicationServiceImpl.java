package com.cailu.bom.erp.application;

import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.Warehouse;
import com.cailu.bom.erp.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseApplicationServiceImpl implements WarehouseApplicationService {
    private final WarehouseRepository repository;

    public List<WarehouseView> list(String keyword, Integer status) {
        return repository.findAll(keyword, status).stream().map(this::view).toList();
    }

    public WarehouseView get(Long id) {
        return repository.findById(id).map(this::view)
                .orElseThrow(() -> new BusinessException("warehouse not found", 404));
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehouseView create(WarehouseCommand command) {
        repository.findByCode(command.code().trim()).ifPresent(w -> {
            throw new BusinessException("仓库编码已存在");
        });
        return view(repository.save(new Warehouse(null, command.code().trim(), command.name().trim(),
                command.address(), command.status() == null ? 1 : command.status())));
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehouseView update(Long id, WarehouseCommand command) {
        Warehouse old = repository.findById(id)
                .orElseThrow(() -> new BusinessException("warehouse not found", 404));
        repository.findByCode(command.code().trim()).filter(w -> !w.id().equals(id)).ifPresent(w -> {
            throw new BusinessException("仓库编码已存在");
        });
        return view(repository.save(new Warehouse(id, command.code().trim(), command.name().trim(),
                command.address(), command.status() == null ? old.status() : command.status())));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new BusinessException("warehouse not found", 404));
        repository.deleteById(id);
    }

    private WarehouseView view(Warehouse w) {
        return new WarehouseView(w.id(), w.code(), w.name(), w.address(), w.status());
    }
}
