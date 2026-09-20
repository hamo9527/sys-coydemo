package com.cailu.bom.system.domain.model;

import lombok.Getter;

@Getter
public class SysUser {

    private final Long id;
    private final String username;
    private final String realName;
    private final String mobile;
    private final Long orgId;
    private final Integer status;

    public SysUser(Long id, String username, String realName, String mobile, Long orgId, Integer status) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.mobile = mobile;
        this.orgId = orgId;
        this.status = status == null ? 1 : status;
    }

    public boolean isEnabled() {
        return Integer.valueOf(1).equals(status);
    }
}
