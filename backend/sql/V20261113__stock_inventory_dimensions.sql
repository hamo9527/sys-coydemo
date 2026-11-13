-- Inventory dimensions are stored with document lines and stock balances.
-- Color values remain free-form strings; suggestions are derived from prior documents.
USE cailu_bom_inventory;

ALTER TABLE erp_purchase_order_item
    ADD COLUMN color_code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '色号',
    ADD COLUMN batch_no VARCHAR(64) NOT NULL DEFAULT '' COMMENT '批次',
    ADD COLUMN grade VARCHAR(32) NOT NULL DEFAULT '' COMMENT '等级',
    ADD COLUMN bin_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '货位';

ALTER TABLE erp_stock
    DROP INDEX uk_erp_stock_wh_prod,
    ADD COLUMN color_code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '色号',
    ADD COLUMN batch_no VARCHAR(64) NOT NULL DEFAULT '' COMMENT '批次',
    ADD COLUMN grade VARCHAR(32) NOT NULL DEFAULT '' COMMENT '等级',
    ADD COLUMN bin_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '货位',
    ADD UNIQUE KEY uk_erp_stock_dimension (warehouse_id, product_id, color_code, batch_no, grade, bin_name),
    ADD KEY idx_erp_stock_product_color (product_id, color_code);

ALTER TABLE erp_stock_ledger
    ADD COLUMN color_code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '色号',
    ADD COLUMN batch_no VARCHAR(64) NOT NULL DEFAULT '' COMMENT '批次',
    ADD COLUMN grade VARCHAR(32) NOT NULL DEFAULT '' COMMENT '等级',
    ADD COLUMN bin_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '货位';

ALTER TABLE erp_outbound_order_item
    ADD COLUMN color_code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '色号',
    ADD KEY idx_erp_outbound_item_product_color (product_id, color_code);

CREATE INDEX idx_erp_purchase_item_product_color
    ON erp_purchase_order_item (product_id, color_code);
