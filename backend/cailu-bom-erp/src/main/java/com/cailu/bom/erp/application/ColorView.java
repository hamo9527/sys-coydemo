package com.cailu.bom.erp.application;

public record ColorView(Long id, String code, String name, String shortName, String brandName,
        String seriesName, String manufacturerColorCode, String swatchImageUrl, Integer status, String remark) {
}
