package com.cailu.bom.system.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.system.application.AuthApplicationService;
import com.cailu.bom.system.application.LoginCommand;
import com.cailu.bom.system.application.LoginView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginView>> login(@Valid @RequestBody LoginRequest request) {
        LoginView view = authApplicationService.login(new LoginCommand(request.username(), request.password()));
        return ResponseEntity.ok(ApiResponse.success(view));
    }
}
