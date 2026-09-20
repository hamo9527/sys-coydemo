package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.CreateOutboundCommand;
import com.cailu.bom.erp.application.OutboundApplicationService;
import com.cailu.bom.erp.application.OutboundView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/erp/outbounds")
@RequiredArgsConstructor
public class OutboundController {

    private final OutboundApplicationService outboundApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OutboundView>> create(@Valid @RequestBody CreateOutboundRequest request) {
        CreateOutboundCommand command = new CreateOutboundCommand(
                request.customNo(),
                request.billDate(),
                request.outboundType(),
                request.orderNo(),
                request.orderCustomNo(),
                request.orderDate(),
                request.orderType(),
                request.customerCode(),
                request.customerName(),
                request.contact(),
                request.mobile(),
                request.community(),
                request.address(),
                request.channel(),
                request.customerLevel(),
                request.salesDept(),
                request.salesperson(),
                request.guide(),
                request.designer(),
                request.designMethod(),
                request.shipFrom(),
                request.deliveryMethod(),
                request.packageProduct(),
                request.handler(),
                request.innerRemark(),
                request.outerRemark(),
                request.totalSqm(),
                request.totalWeight(),
                request.items().stream()
                        .map(i -> new CreateOutboundCommand.Item(
                                i.position(), i.productId(), i.sku(), i.productName(), i.brand(), i.spec(),
                                i.grade(), i.batchNo(), i.setsQty(), i.boxes(), i.pieces(), i.qty(), i.unit(),
                                i.stdPrice(), i.discount(), i.price(), i.warehouseId(), i.warehouseCode(),
                                i.warehouseName(), i.binName(), i.feeType(),
                                Boolean.TRUE.equals(i.occupy()),
                                i.countPerf() == null || Boolean.TRUE.equals(i.countPerf())))
                        .toList());
        return ResponseEntity.ok(ApiResponse.success(outboundApplicationService.create(command)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OutboundView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(outboundApplicationService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<OutboundView>>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(outboundApplicationService.list(page, size)));
    }

    @PostMapping("/{id}/confirmations")
    public ResponseEntity<ApiResponse<OutboundView>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(outboundApplicationService.confirm(id)));
    }
}
