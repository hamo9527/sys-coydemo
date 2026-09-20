package com.cailu.bom.system.interfaces;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6, max = 64) String password,
        String realName,
        String mobile,
        Long orgId
) {
}
