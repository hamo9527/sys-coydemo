-- Add unit cost to stock count lines for gain/loss amount calculation.
USE cailu_bom_inventory;

ALTER TABLE erp_stock_count_item
    ADD COLUMN cost_price DECIMAL(18, 4) NOT NULL DEFAULT 0 COMMENT '成本单价' AFTER bin_name;
