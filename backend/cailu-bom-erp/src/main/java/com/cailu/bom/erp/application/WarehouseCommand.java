package com.cailu.bom.erp.application;

public record WarehouseCommand(String code, String name, String address, Integer status) {
    public WarehouseCommand {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("仓库编码不能为空");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("仓库名称不能为空");
    }
}
