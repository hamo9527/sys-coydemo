package com.cailu.bom.finance.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.finance.application.CreateReceivableCommand;
import com.cailu.bom.finance.application.ReceivableApplicationService;
import com.cailu.bom.finance.application.ReceivableView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/receivables")
@RequiredArgsConstructor
public class ReceivableController {

    private final ReceivableApplicationService receivableApplicationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReceivableView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(receivableApplicationService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReceivableView>> create(@Valid @RequestBody CreateReceivableRequest request) {
        CreateReceivableCommand command = new CreateReceivableCommand(
                request.billNo(), request.customerId(), request.sourceOrderId(),
                request.amount(), request.dueDate());
        return ResponseEntity.ok(ApiResponse.success(receivableApplicationService.create(command)));
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<ApiResponse<ReceivableView>> receive(
            @PathVariable Long id,
            @Valid @RequestBody ReceivePaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                receivableApplicationService.receive(id, request.amount())));
    }
}
