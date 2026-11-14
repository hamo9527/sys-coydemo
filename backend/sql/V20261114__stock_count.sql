-- Stock count snapshots are based on business dates, not record creation timestamps.
USE cailu_bom_inventory;

ALTER TABLE erp_stock_ledger
    ADD COLUMN biz_date DATE NULL COMMENT '业务日期' AFTER biz_type,
    ADD KEY idx_erp_ledger_wh_date (warehouse_id, biz_date);

UPDATE erp_stock_ledger
SET biz_date = DATE(created_at)
WHERE biz_date IS NULL;

UPDATE erp_stock_ledger ledger
JOIN erp_outbound_order outbound ON outbound.id = ledger.biz_id
SET ledger.biz_date = outbound.bill_date
WHERE ledger.biz_type = 'sales_outbound';

CREATE TABLE erp_stock_count (
    id BIGINT NOT NULL PRIMARY KEY,
    bill_no VARCHAR(64) NOT NULL,
    warehouse_id BIGINT NOT NULL,
    bill_date DATE NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'draft' COMMENT 'draft/confirmed',
    remark VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_stock_count_bill (bill_no),
    KEY idx_erp_stock_count_warehouse_date (warehouse_id, bill_date),
    KEY idx_erp_stock_count_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE erp_stock_count_item (
    id BIGINT NOT NULL PRIMARY KEY,
    count_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    color_code VARCHAR(64) NOT NULL DEFAULT '',
    batch_no VARCHAR(64) NOT NULL DEFAULT '',
    grade VARCHAR(32) NOT NULL DEFAULT '',
    bin_name VARCHAR(64) NOT NULL DEFAULT '',
    book_qty DECIMAL(18, 4) NOT NULL DEFAULT 0,
    actual_qty DECIMAL(18, 4) NULL,
    difference_qty DECIMAL(18, 4) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_erp_stock_count_item_count (count_id),
    KEY idx_erp_stock_count_item_dimension (warehouse_id, product_id, color_code, batch_no, grade, bin_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
