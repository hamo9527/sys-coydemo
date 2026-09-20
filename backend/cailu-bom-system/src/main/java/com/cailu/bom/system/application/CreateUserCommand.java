package com.cailu.bom.system.application;

public record CreateUserCommand(
        String username,
        String password,
        String realName,
        String mobile,
        Long orgId
) {
    public CreateUserCommand {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("password min length 6");
        }
    }
}
