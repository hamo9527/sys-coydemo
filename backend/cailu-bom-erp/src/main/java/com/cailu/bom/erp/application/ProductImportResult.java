package com.cailu.bom.erp.application;

import java.util.List;

public record ProductImportResult(
        int successCount,
        int failureCount,
        List<String> errors
) {
}
