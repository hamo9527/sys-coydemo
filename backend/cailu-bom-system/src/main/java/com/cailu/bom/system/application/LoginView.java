package com.cailu.bom.system.application;

public record LoginView(
        String token,
        UserView user
) {
}
