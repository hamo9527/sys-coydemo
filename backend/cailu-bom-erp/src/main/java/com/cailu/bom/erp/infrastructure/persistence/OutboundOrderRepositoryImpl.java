package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.domain.model.OutboundOrder;
import com.cailu.bom.erp.domain.model.OutboundOrderItem;
import com.cailu.bom.erp.domain.repository.OutboundOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OutboundOrderRepositoryImpl implements OutboundOrderRepository {

    private final ErpOutboundOrderMapper erpOutboundOrderMapper;
    private final ErpOutboundOrderItemMapper erpOutboundOrderItemMapper;

    @Override
    public Optional<OutboundOrder> findById(Long id) {
        ErpOutboundOrderDO row = erpOutboundOrderMapper.selectById(id);
        if (row == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public Optional<OutboundOrder> findByBillNo(String billNo) {
        ErpOutboundOrderDO row = erpOutboundOrderMapper.selectOne(new LambdaQueryWrapper<ErpOutboundOrderDO>()
                .eq(ErpOutboundOrderDO::getBillNo, billNo)
                .last("LIMIT 1"));
        if (row == null) {
            return Optional.empty();
        }
        return Optional.of(toDomain(row, loadItems(row.getId())));
    }

    @Override
    public PageResult<OutboundOrder> page(long page, long size) {
        long current = Math.max(page, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);
        Page<ErpOutboundOrderDO> result = erpOutboundOrderMapper.selectPage(
                new Page<>(current, pageSize),
                new LambdaQueryWrapper<ErpOutboundOrderDO>().orderByDesc(ErpOutboundOrderDO::getId));
        List<OutboundOrder> records = result.getRecords().stream()
                .map(row -> toDomain(row, loadItems(row.getId())))
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public OutboundOrder save(OutboundOrder order) {
        ErpOutboundOrderDO row = toRow(order);
        if (row.getId() == null) {
            erpOutboundOrderMapper.insert(row);
            int line = 1;
            for (OutboundOrderItem item : order.getItems()) {
                erpOutboundOrderItemMapper.insert(toItemRow(row.getId(), line++, item));
            }
        } else {
            erpOutboundOrderMapper.updateById(row);
            erpOutboundOrderItemMapper.delete(new LambdaQueryWrapper<ErpOutboundOrderItemDO>()
                    .eq(ErpOutboundOrderItemDO::getOrderId, row.getId()));
            int line = 1;
            for (OutboundOrderItem item : order.getItems()) {
                erpOutboundOrderItemMapper.insert(toItemRow(row.getId(), line++, item));
            }
        }
        return toDomain(row, loadItems(row.getId()));
    }

    @Override
    public OutboundOrder updateStatus(Long id, String status) {
        ErpOutboundOrderDO row = erpOutboundOrderMapper.selectById(id);
        if (row == null) {
            throw new IllegalArgumentException("outbound order not found");
        }
        row.setStatus(status);
        erpOutboundOrderMapper.updateById(row);
        return toDomain(row, loadItems(row.getId()));
    }

    private List<OutboundOrderItem> loadItems(Long orderId) {
        return erpOutboundOrderItemMapper.selectList(new LambdaQueryWrapper<ErpOutboundOrderItemDO>()
                        .eq(ErpOutboundOrderItemDO::getOrderId, orderId)
                        .orderByAsc(ErpOutboundOrderItemDO::getLineNo)
                        .orderByAsc(ErpOutboundOrderItemDO::getId))
                .stream()
                .map(this::toItemDomain)
                .toList();
    }

    private ErpOutboundOrderDO toRow(OutboundOrder order) {
        ErpOutboundOrderDO row = new ErpOutboundOrderDO();
        row.setId(order.getId());
        row.setBillNo(order.getBillNo());
        row.setCustomNo(order.getCustomNo());
        row.setBillDate(order.getBillDate());
        row.setOutboundType(order.getOutboundType());
        row.setOrderNo(order.getOrderNo());
        row.setOrderCustomNo(order.getOrderCustomNo());
        row.setOrderDate(order.getOrderDate());
        row.setOrderType(order.getOrderType());
        row.setCustomerCode(order.getCustomerCode());
        row.setCustomerName(order.getCustomerName());
        row.setContact(order.getContact());
        row.setMobile(order.getMobile());
        row.setCommunity(order.getCommunity());
        row.setAddress(order.getAddress());
        row.setChannel(order.getChannel());
        row.setCustomerLevel(order.getCustomerLevel());
        row.setSalesDept(order.getSalesDept());
        row.setSalesperson(order.getSalesperson());
        row.setGuide(order.getGuide());
        row.setDesigner(order.getDesigner());
        row.setDesignMethod(order.getDesignMethod());
        row.setShipFrom(order.getShipFrom());
        row.setDeliveryMethod(order.getDeliveryMethod());
        row.setPackageProduct(order.getPackageProduct());
        row.setHandler(order.getHandler());
        row.setInnerRemark(order.getInnerRemark());
        row.setOuterRemark(order.getOuterRemark());
        row.setStatus(order.getStatus());
        row.setTotalAmount(order.getTotalAmount());
        row.setTotalQty(order.getTotalQty());
        row.setTotalBoxes(order.getTotalBoxes());
        row.setTotalSqm(order.getTotalSqm());
        row.setTotalWeight(order.getTotalWeight());
        return row;
    }

    private ErpOutboundOrderItemDO toItemRow(Long orderId, int lineNo, OutboundOrderItem item) {
        ErpOutboundOrderItemDO row = new ErpOutboundOrderItemDO();
        row.setOrderId(orderId);
        row.setLineNo(lineNo);
        row.setPosition(item.getPosition());
        row.setProductId(item.getProductId());
        row.setSku(item.getSku());
        row.setProductName(item.getProductName());
        row.setBrand(item.getBrand());
        row.setSpec(item.getSpec());
        row.setGrade(item.getGrade());
        row.setBatchNo(item.getBatchNo());
        row.setSetsQty(item.getSetsQty());
        row.setBoxes(item.getBoxes());
        row.setPieces(item.getPieces());
        row.setQty(item.getQty());
        row.setUnit(item.getUnit());
        row.setStdPrice(item.getStdPrice());
        row.setDiscount(item.getDiscount());
        row.setPrice(item.getPrice());
        row.setAmount(item.getAmount());
        row.setWarehouseId(item.getWarehouseId());
        row.setWarehouseCode(item.getWarehouseCode());
        row.setWarehouseName(item.getWarehouseName());
        row.setBinName(item.getBinName());
        row.setFeeType(item.getFeeType());
        row.setOccupy(item.isOccupy() ? 1 : 0);
        row.setCountPerf(item.isCountPerf() ? 1 : 0);
        return row;
    }

    private OutboundOrderItem toItemDomain(ErpOutboundOrderItemDO row) {
        return new OutboundOrderItem(
                row.getId(), row.getLineNo(), row.getPosition(), row.getProductId(), row.getSku(),
                row.getProductName(), row.getBrand(), row.getSpec(), row.getGrade(), row.getBatchNo(),
                row.getSetsQty(), row.getBoxes(), row.getPieces(), row.getQty(), row.getUnit(),
                row.getStdPrice(), row.getDiscount(), row.getPrice(), row.getAmount(),
                row.getWarehouseId(), row.getWarehouseCode(), row.getWarehouseName(), row.getBinName(),
                row.getFeeType(), Integer.valueOf(1).equals(row.getOccupy()),
                !Integer.valueOf(0).equals(row.getCountPerf()));
    }

    private OutboundOrder toDomain(ErpOutboundOrderDO row, List<OutboundOrderItem> items) {
        return new OutboundOrder(
                row.getId(), row.getBillNo(), row.getCustomNo(), row.getBillDate(), row.getOutboundType(),
                row.getOrderNo(), row.getOrderCustomNo(), row.getOrderDate(), row.getOrderType(),
                row.getCustomerCode(), row.getCustomerName(), row.getContact(), row.getMobile(),
                row.getCommunity(), row.getAddress(), row.getChannel(), row.getCustomerLevel(),
                row.getSalesDept(), row.getSalesperson(), row.getGuide(), row.getDesigner(),
                row.getDesignMethod(), row.getShipFrom(), row.getDeliveryMethod(), row.getPackageProduct(),
                row.getHandler(), row.getInnerRemark(), row.getOuterRemark(), row.getStatus(),
                row.getTotalAmount(), row.getTotalQty(), row.getTotalBoxes(), row.getTotalSqm(),
                row.getTotalWeight(), items);
    }
}
