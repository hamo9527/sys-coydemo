package com.cailu.bom.system.application;

public record UserView(
        Long id,
        String username,
        String realName,
        String mobile,
        Long orgId,
        Integer status
) {
}
