package com.cailu.bom.erp.application;

public record ProductColorModeCommand(
        String colorManagementMode,
        Boolean batchManagementEnabled,
        Boolean gradeManagementEnabled
) {
    public ProductColorModeCommand {
        if (!"NONE".equals(colorManagementMode) && !"DYNAMIC".equals(colorManagementMode)) {
            throw new IllegalArgumentException("色号管理模式必须为 NONE 或 DYNAMIC");
        }
    }
}
