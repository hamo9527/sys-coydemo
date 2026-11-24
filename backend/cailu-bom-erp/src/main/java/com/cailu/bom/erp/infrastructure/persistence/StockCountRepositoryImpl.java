package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.StockCount;
import com.cailu.bom.erp.domain.model.StockCountItem;
import com.cailu.bom.erp.domain.repository.StockCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockCountRepositoryImpl implements StockCountRepository {

    private final ErpStockCountMapper countMapper;
    private final ErpStockCountItemMapper itemMapper;

    @Override
    public PageResult<StockCount> page(long page, long size) {
        long current = Math.max(page, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);
        Page<ErpStockCountDO> result = countMapper.selectPage(
                new Page<>(current, pageSize),
                new LambdaQueryWrapper<ErpStockCountDO>().orderByDesc(ErpStockCountDO::getBillDate)
                        .orderByDesc(ErpStockCountDO::getId));
        List<Long> countIds = result.getRecords().stream().map(ErpStockCountDO::getId).toList();
        Map<Long, List<StockCountItem>> itemsByCountId = countIds.isEmpty() ? Map.of()
                : itemMapper.selectList(new LambdaQueryWrapper<ErpStockCountItemDO>()
                        .in(ErpStockCountItemDO::getCountId, countIds)
                        .orderByAsc(ErpStockCountItemDO::getId))
                .stream()
                .collect(Collectors.groupingBy(ErpStockCountItemDO::getCountId,
                        Collectors.mapping(this::toDomainItem, Collectors.toList())));
        return new PageResult<>(result.getRecords().stream()
                .map(row -> toDomain(row, itemsByCountId.getOrDefault(row.getId(), List.of())))
                .toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<StockCount> findById(Long id) {
        ErpStockCountDO row = countMapper.selectById(id);
        return row == null ? Optional.empty() : Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public Optional<StockCount> findByIdForUpdate(Long id) {
        ErpStockCountDO row = countMapper.selectOne(new LambdaQueryWrapper<ErpStockCountDO>()
                .eq(ErpStockCountDO::getId, id).last("LIMIT 1 FOR UPDATE"));
        return row == null ? Optional.empty() : Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public Optional<StockCount> findByBillNo(String billNo) {
        ErpStockCountDO row = countMapper.selectOne(new LambdaQueryWrapper<ErpStockCountDO>()
                .eq(ErpStockCountDO::getBillNo, billNo).last("LIMIT 1"));
        return row == null ? Optional.empty() : Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public StockCount save(StockCount stockCount) {
        ErpStockCountDO row = new ErpStockCountDO();
        row.setBillNo(stockCount.billNo());
        row.setWarehouseId(stockCount.warehouseId());
        row.setBillDate(stockCount.billDate());
        row.setStatus(stockCount.status());
        row.setRemark(stockCount.remark());
        countMapper.insert(row);
        replaceItems(row.getId(), stockCount.warehouseId(), stockCount.items());
        return toDomain(row, loadItems(row.getId()));
    }

    @Override
    public void replaceItems(Long countId, Long warehouseId, List<StockCountItem> items) {
        itemMapper.delete(new LambdaQueryWrapper<ErpStockCountItemDO>()
                .eq(ErpStockCountItemDO::getCountId, countId));
        for (StockCountItem item : items) {
            ErpStockCountItemDO itemRow = new ErpStockCountItemDO();
            itemRow.setCountId(countId);
            itemRow.setWarehouseId(warehouseId);
            itemRow.setProductId(item.productId());
            itemRow.setColorCode(normalize(item.colorCode()));
            itemRow.setBatchNo(normalize(item.batchNo()));
            itemRow.setGrade(normalize(item.grade()));
            itemRow.setBinName(normalize(item.binName()));
            itemRow.setCostPrice(item.costPrice());
            itemRow.setBookQty(item.bookQty());
            itemRow.setActualQty(item.actualQty());
            itemRow.setDifferenceQty(item.differenceQty());
            itemMapper.insert(itemRow);
        }
    }

    @Override
    public StockCount updateStatus(Long id, String status) {
        ErpStockCountDO row = countMapper.selectById(id);
        if (row == null) {
            throw new IllegalArgumentException("盘点单不存在");
        }
        row.setStatus(status);
        countMapper.updateById(row);
        return toDomain(row, loadItems(id));
    }

    private List<StockCountItem> loadItems(Long countId) {
        return itemMapper.selectList(new LambdaQueryWrapper<ErpStockCountItemDO>()
                        .eq(ErpStockCountItemDO::getCountId, countId)
                        .orderByAsc(ErpStockCountItemDO::getId))
                .stream().map(this::toDomainItem).toList();
    }

    private StockCountItem toDomainItem(ErpStockCountItemDO row) {
        return new StockCountItem(row.getId(), row.getProductId(), row.getColorCode(),
                row.getBatchNo(), row.getGrade(), row.getBinName(), row.getCostPrice(),
                row.getBookQty(), row.getActualQty(), row.getDifferenceQty());
    }

    private StockCount toDomain(ErpStockCountDO row, List<StockCountItem> items) {
        return new StockCount(row.getId(), row.getBillNo(), row.getWarehouseId(), row.getBillDate(),
                row.getStatus(), row.getRemark(), items);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
