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
        return new PageResult<>(
                result.getRecords().stream().map(row -> toDomain(row, loadItems(row.getId()))).toList(),
                result.getTotal(), result.getCurrent(), result.getSize());
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
        for (StockCountItem item : stockCount.items()) {
            ErpStockCountItemDO itemRow = new ErpStockCountItemDO();
            itemRow.setCountId(row.getId());
            itemRow.setWarehouseId(stockCount.warehouseId());
            itemRow.setProductId(item.productId());
            itemRow.setColorCode(item.colorCode());
            itemRow.setBatchNo(item.batchNo());
            itemRow.setGrade(item.grade());
            itemRow.setBinName(item.binName());
            itemRow.setBookQty(item.bookQty());
            itemRow.setActualQty(item.actualQty());
            itemRow.setDifferenceQty(item.differenceQty());
            itemMapper.insert(itemRow);
        }
        return toDomain(row, loadItems(row.getId()));
    }

    @Override
    public void updateActualQuantities(Long countId, List<ActualQuantity> quantities) {
        for (ActualQuantity quantity : quantities) {
            ErpStockCountItemDO row = itemMapper.selectById(quantity.itemId());
            if (row == null || !countId.equals(row.getCountId())) {
                throw new IllegalArgumentException("盘点明细不存在");
            }
            row.setActualQty(quantity.actualQty());
            row.setDifferenceQty(quantity.actualQty().subtract(row.getBookQty()));
            itemMapper.updateById(row);
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
                .stream()
                .map(row -> new StockCountItem(row.getId(), row.getProductId(), row.getColorCode(),
                        row.getBatchNo(), row.getGrade(), row.getBinName(), row.getBookQty(),
                        row.getActualQty(), row.getDifferenceQty()))
                .toList();
    }

    private StockCount toDomain(ErpStockCountDO row, List<StockCountItem> items) {
        return new StockCount(row.getId(), row.getBillNo(), row.getWarehouseId(), row.getBillDate(),
                row.getStatus(), row.getRemark(), items);
    }
}
