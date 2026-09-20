package com.cailu.bom.erp.application;

import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.erp.domain.model.OutboundOrder;
import com.cailu.bom.erp.domain.model.OutboundOrderItem;
import com.cailu.bom.erp.domain.repository.OutboundOrderRepository;
import com.cailu.bom.erp.domain.repository.ProductRepository;
import com.cailu.bom.erp.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OutboundApplicationServiceImpl implements OutboundApplicationService {

    private static final String BIZ_TYPE_SALES_OUTBOUND = "sales_outbound";

    private final OutboundOrderRepository outboundOrderRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OutboundView create(CreateOutboundCommand command) {
        String billNo = nextBillNo();
        while (outboundOrderRepository.findByBillNo(billNo).isPresent()) {
            billNo = nextBillNo();
        }

        List<OutboundOrderItem> items = new ArrayList<>();
        int line = 1;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalBoxes = BigDecimal.ZERO;
        for (CreateOutboundCommand.Item item : command.items()) {
            Long productId = resolveProductId(item);
            BigDecimal amount = item.qty().multiply(item.price() == null ? BigDecimal.ZERO : item.price());
            OutboundOrderItem domainItem = new OutboundOrderItem(
                    null, line++, item.position(), productId, item.sku(), item.productName(), item.brand(),
                    item.spec(), item.grade(), item.batchNo(), item.setsQty(), item.boxes(), item.pieces(),
                    item.qty(), item.unit(), item.stdPrice(), item.discount(), item.price(), amount,
                    item.warehouseId(), item.warehouseCode(), item.warehouseName(), item.binName(),
                    item.feeType(), item.occupy(), item.countPerf());
            items.add(domainItem);
            totalAmount = totalAmount.add(amount);
            totalQty = totalQty.add(item.qty());
            totalBoxes = totalBoxes.add(item.boxes() == null ? BigDecimal.ZERO : item.boxes());
        }

        OutboundOrder order = new OutboundOrder(
                null, billNo, command.customNo(), command.billDate(), command.outboundType(),
                command.orderNo(), command.orderCustomNo(), command.orderDate(), command.orderType(),
                command.customerCode(), command.customerName(), command.contact(), command.mobile(),
                command.community(), command.address(), command.channel(), command.customerLevel(),
                command.salesDept(), command.salesperson(), command.guide(), command.designer(),
                command.designMethod(), command.shipFrom(), command.deliveryMethod(), command.packageProduct(),
                command.handler(), command.innerRemark(), command.outerRemark(), "draft",
                totalAmount, totalQty, totalBoxes,
                command.totalSqm() == null ? BigDecimal.ZERO : command.totalSqm(),
                command.totalWeight() == null ? BigDecimal.ZERO : command.totalWeight(),
                items);
        return toView(outboundOrderRepository.save(order));
    }

    @Override
    public OutboundView getById(Long id) {
        return toView(outboundOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("outbound order not found", 404)));
    }

    @Override
    public PageResult<OutboundView> list(long page, long size) {
        PageResult<OutboundOrder> result = outboundOrderRepository.page(page, size);
        return new PageResult<>(
                result.records().stream().map(this::toView).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OutboundView confirm(Long id) {
        OutboundOrder order = outboundOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("outbound order not found", 404));
        OutboundOrder effective;
        try {
            effective = order.markEffective();
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ex.getMessage());
        }

        for (OutboundOrderItem item : order.getItems()) {
            if (item.getProductId() == null || item.getWarehouseId() == null) {
                continue;
            }
            try {
                stockRepository.decrease(
                        item.getWarehouseId(),
                        item.getProductId(),
                        item.getQty(),
                        BIZ_TYPE_SALES_OUTBOUND,
                        order.getId(),
                        order.getBillNo());
            } catch (IllegalArgumentException ex) {
                throw new BusinessException(ex.getMessage());
            }
        }
        return toView(outboundOrderRepository.updateStatus(effective.getId(), effective.getStatus()));
    }

    private Long resolveProductId(CreateOutboundCommand.Item item) {
        if (item.productId() != null) {
            return item.productId();
        }
        return productRepository.findBySku(item.sku())
                .map(p -> p.getId())
                .orElse(null);
    }

    private String nextBillNo() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int seq = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "CK-" + date.substring(0, 6) + String.format("%04d", seq);
    }

    private OutboundView toView(OutboundOrder order) {
        List<OutboundItemView> items = order.getItems().stream()
                .map(i -> new OutboundItemView(
                        i.getId(), i.getLineNo(), i.getPosition(), i.getProductId(), i.getSku(),
                        i.getProductName(), i.getBrand(), i.getSpec(), i.getGrade(), i.getBatchNo(),
                        i.getSetsQty(), i.getBoxes(), i.getPieces(), i.getQty(), i.getUnit(),
                        i.getStdPrice(), i.getDiscount(), i.getPrice(), i.getAmount(),
                        i.getWarehouseId(), i.getWarehouseCode(), i.getWarehouseName(), i.getBinName(),
                        i.getFeeType(), i.isOccupy(), i.isCountPerf()))
                .toList();
        return new OutboundView(
                order.getId(), order.getBillNo(), order.getCustomNo(), order.getBillDate(),
                order.getOutboundType(), order.getOrderNo(), order.getOrderCustomNo(), order.getOrderDate(),
                order.getOrderType(), order.getCustomerCode(), order.getCustomerName(), order.getContact(),
                order.getMobile(), order.getCommunity(), order.getAddress(), order.getChannel(),
                order.getCustomerLevel(), order.getSalesDept(), order.getSalesperson(), order.getGuide(),
                order.getDesigner(), order.getDesignMethod(), order.getShipFrom(), order.getDeliveryMethod(),
                order.getPackageProduct(), order.getHandler(), order.getInnerRemark(), order.getOuterRemark(),
                order.getStatus(), order.getTotalAmount(), order.getTotalQty(), order.getTotalBoxes(),
                order.getTotalSqm(), order.getTotalWeight(), items);
    }
}
