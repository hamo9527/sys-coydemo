package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.Stock;
import com.cailu.bom.erp.domain.model.StockCount;
import com.cailu.bom.erp.domain.model.StockCountItem;
import com.cailu.bom.erp.domain.repository.StockCountRepository;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockCountApplicationServiceImpl implements StockCountApplicationService {

    private static final String BIZ_TYPE_COUNT_GAIN = "stock_count_gain";
    private static final String BIZ_TYPE_COUNT_LOSS = "stock_count_loss";

    private final StockCountRepository stockCountRepository;
    private final StockRepository stockRepository;

    @Override
    public PageResult<StockCountView> list(long page, long size) {
        PageResult<StockCount> result = stockCountRepository.page(page, size);
        return new PageResult<>(result.records().stream().map(this::toView).toList(),
                result.total(), result.page(), result.size());
    }

    @Override
    public StockCountView get(Long id) {
        return toView(stockCountRepository.findById(id)
                .orElseThrow(() -> new BusinessException("盘点单不存在", 404)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockCountView create(CreateStockCountCommand command) {
        if (command.billDate().isAfter(LocalDate.now())) {
            throw new BusinessException("盘点日期不能晚于今天");
        }
        List<StockCountItem> items = stockRepository.balancesAtDate(command.warehouseId(), command.billDate())
                .stream()
                .map(stock -> new StockCountItem(null, stock.getProductId(), stock.getColorCode(),
                        stock.getBatchNo(), stock.getGrade(), stock.getBinName(),
                        stock.getQuantity(), null, null))
                .toList();
        String billNo = nextBillNo(command.billDate());
        while (stockCountRepository.findByBillNo(billNo).isPresent()) {
            billNo = nextBillNo(command.billDate());
        }
        StockCount count = new StockCount(null, billNo, command.warehouseId(), command.billDate(),
                "draft", command.remark(), items);
        return toView(stockCountRepository.save(count));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockCountView updateActualQuantities(Long id, UpdateStockCountCommand command) {
        StockCount count = stockCountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new BusinessException("盘点单不存在", 404));
        if (!"draft".equals(count.status())) {
            throw new BusinessException("只有草稿盘点单可以修改");
        }
        if (command.items().size() != count.items().size()) {
            throw new BusinessException("实盘明细数量与盘点单不一致");
        }
        Map<Long, UpdateStockCountCommand.Item> actuals = command.items().stream()
                .collect(Collectors.toMap(UpdateStockCountCommand.Item::itemId, Function.identity(),
                        (first, second) -> {
                            throw new BusinessException("盘点明细存在重复行");
                        }));
        List<StockCountRepository.ActualQuantity> quantities = count.items().stream().map(item -> {
            UpdateStockCountCommand.Item actual = actuals.remove(item.id());
            if (actual == null) {
                throw new BusinessException("盘点明细缺少实盘数量");
            }
            return new StockCountRepository.ActualQuantity(item.id(), actual.actualQty());
        }).toList();
        if (!actuals.isEmpty()) {
            throw new BusinessException("盘点明细包含无效行");
        }
        stockCountRepository.updateActualQuantities(id, quantities);
        return get(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockCountView confirm(Long id) {
        StockCount count = stockCountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new BusinessException("盘点单不存在", 404));
        if (!"draft".equals(count.status())) {
            throw new BusinessException("只有草稿盘点单可以确认");
        }
        for (StockCountItem item : count.items()) {
            if (item.actualQty() == null) {
                throw new BusinessException("请先填写全部商品的实盘数量");
            }
            BigDecimal difference = item.actualQty().subtract(item.bookQty());
            if (difference.signum() > 0) {
                stockRepository.increase(count.warehouseId(), item.productId(), item.colorCode(),
                        item.batchNo(), item.grade(), item.binName(), difference,
                        BIZ_TYPE_COUNT_GAIN, count.id(), count.billDate(), count.billNo());
            } else if (difference.signum() < 0) {
                try {
                    stockRepository.decrease(count.warehouseId(), item.productId(), item.colorCode(),
                            item.batchNo(), item.grade(), item.binName(), difference.abs(),
                            BIZ_TYPE_COUNT_LOSS, count.id(), count.billDate(), count.billNo());
                } catch (IllegalArgumentException ex) {
                    throw new BusinessException("盘亏数量超过当前可用库存，无法过账：" + item.productId());
                }
            }
        }
        return toView(stockCountRepository.updateStatus(id, "confirmed"));
    }

    private String nextBillNo(LocalDate billDate) {
        String date = billDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        int sequence = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "PD-" + date + String.format("%04d", sequence);
    }

    private StockCountView toView(StockCount count) {
        List<StockCountView.Item> items = count.items().stream()
                .map(item -> new StockCountView.Item(item.id(), item.productId(), item.colorCode(),
                        item.batchNo(), item.grade(), item.binName(), item.bookQty(),
                        item.actualQty(), item.differenceQty()))
                .toList();
        return new StockCountView(count.id(), count.billNo(), count.warehouseId(),
                count.billDate(), count.status(), count.remark(), items);
    }
}
