-- Expand product master data to match the 商品档案 field specification.
-- Apply once to existing cailu_bom_inventory databases after V20260813__init.sql.
USE cailu_bom_inventory;

UPDATE erp_product SET unit = '' WHERE unit IS NULL;

ALTER TABLE erp_product
    MODIFY COLUMN unit VARCHAR(16) NOT NULL DEFAULT '' COMMENT '单位';

ALTER TABLE erp_product
    ADD COLUMN mnemonic_code VARCHAR(64) NULL COMMENT '助记码' AFTER sku,
    ADD COLUMN brand_name VARCHAR(128) NOT NULL DEFAULT '' COMMENT '品牌名称' AFTER name,
    ADD COLUMN auxiliary_unit VARCHAR(16) NULL COMMENT '辅助单位' AFTER unit,
    ADD COLUMN auxiliary_spec VARCHAR(128) NULL COMMENT '辅助规格' AFTER auxiliary_unit,
    ADD COLUMN packaging_unit VARCHAR(16) NULL COMMENT '包装单位' AFTER auxiliary_spec,
    ADD COLUMN packaging_spec VARCHAR(128) NULL COMMENT '包装规格' AFTER packaging_unit,
    ADD COLUMN unit_weight DECIMAL(18,4) NULL COMMENT '单重' AFTER packaging_spec,
    ADD COLUMN minimum_order_qty DECIMAL(18,4) NULL COMMENT '最低起订量' AFTER unit_weight,
    ADD COLUMN min_stock DECIMAL(18,4) NULL COMMENT '最小库存' AFTER minimum_order_qty,
    ADD COLUMN max_stock DECIMAL(18,4) NULL COMMENT '最大库存' AFTER min_stock,
    ADD COLUMN small_color_stock TINYINT(1) NOT NULL DEFAULT 0 COMMENT '小色号库存' AFTER max_stock,
    ADD COLUMN stock_age_threshold INT NULL COMMENT '库龄阈值（天）' AFTER small_color_stock,
    ADD COLUMN obsolete_days INT NULL COMMENT '呆滞天数' AFTER stock_age_threshold,
    ADD COLUMN shelf_life_days INT NULL COMMENT '保质天数' AFTER obsolete_days,
    ADD COLUMN product_attribute VARCHAR(64) NOT NULL DEFAULT 'normal' COMMENT '商品属性' AFTER shelf_life_days,
    ADD COLUMN virtual_product TINYINT(1) NOT NULL DEFAULT 0 COMMENT '虚拟商品' AFTER product_attribute,
    ADD COLUMN installation_required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '包安装' AFTER virtual_product,
    ADD COLUMN cloud_store_visible TINYINT(1) NOT NULL DEFAULT 0 COMMENT '云店可查' AFTER installation_required,
    ADD COLUMN production_process VARCHAR(255) NULL COMMENT '生产工艺' AFTER cloud_store_visible,
    ADD COLUMN production_origin VARCHAR(128) NULL COMMENT '生产产地' AFTER production_process,
    ADD COLUMN usage_location VARCHAR(128) NULL COMMENT '使用位置' AFTER production_origin,
    ADD COLUMN vr_url VARCHAR(512) NULL COMMENT 'VR地址' AFTER usage_location,
    ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注' AFTER vr_url;

CREATE INDEX idx_erp_product_mnemonic_code ON erp_product (mnemonic_code);
CREATE INDEX idx_erp_product_brand_name ON erp_product (brand_name);
