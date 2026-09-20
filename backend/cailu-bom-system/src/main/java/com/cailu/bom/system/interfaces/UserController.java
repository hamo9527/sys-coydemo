package com.cailu.bom.system.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.system.application.CreateUserCommand;
import com.cailu.bom.system.application.UserApplicationService;
import com.cailu.bom.system.application.UserView;
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
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userApplicationService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserView>> create(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(
                request.username(), request.password(), request.realName(),
                request.mobile(), request.orgId());
        return ResponseEntity.ok(ApiResponse.success(userApplicationService.create(command)));
    }
}
