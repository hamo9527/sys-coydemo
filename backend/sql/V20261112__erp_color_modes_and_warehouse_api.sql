USE cailu_bom;

ALTER TABLE erp_product
    ADD COLUMN color_management_mode VARCHAR(16) NOT NULL DEFAULT 'NONE' COMMENT 'NONE无色号/DYNAMIC动态色号/BOUND档案绑定',
    ADD COLUMN batch_management_enabled TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用批次管理',
    ADD COLUMN grade_management_enabled TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用等级管理';

CREATE TABLE erp_color (
    id BIGINT NOT NULL PRIMARY KEY,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    short_name VARCHAR(64) NULL,
    brand_name VARCHAR(128) NULL,
    series_name VARCHAR(128) NULL,
    manufacturer_color_code VARCHAR(64) NULL,
    swatch_image_url VARCHAR(512) NULL,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(500) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_color_code (code),
    KEY idx_erp_color_name (name),
    KEY idx_erp_color_brand (brand_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE erp_product_color (
    id BIGINT NOT NULL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    allow_inbound TINYINT(1) NOT NULL DEFAULT 1,
    allow_outbound TINYINT(1) NOT NULL DEFAULT 1,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_product_color (product_id, color_id),
    KEY idx_erp_product_color_color (color_id),
    CONSTRAINT fk_erp_product_color_product FOREIGN KEY (product_id) REFERENCES erp_product(id),
    CONSTRAINT fk_erp_product_color_color FOREIGN KEY (color_id) REFERENCES erp_color(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
