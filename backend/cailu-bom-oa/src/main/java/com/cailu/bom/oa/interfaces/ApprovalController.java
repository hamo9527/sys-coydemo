package com.cailu.bom.oa.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.oa.application.ApprovalApplicationService;
import com.cailu.bom.oa.application.ApprovalView;
import com.cailu.bom.oa.application.CreateApprovalCommand;
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
@RequestMapping("/api/oa/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalApplicationService approvalApplicationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApprovalView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(approvalApplicationService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApprovalView>> create(@Valid @RequestBody CreateApprovalRequest request) {
        CreateApprovalCommand command = new CreateApprovalCommand(
                request.title(), request.bizType(), request.bizId(),
                request.applicantId(), request.remark());
        return ResponseEntity.ok(ApiResponse.success(approvalApplicationService.create(command)));
    }

    @PostMapping("/{id}/submissions")
    public ResponseEntity<ApiResponse<ApprovalView>> submit(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(approvalApplicationService.submit(id)));
    }
}
