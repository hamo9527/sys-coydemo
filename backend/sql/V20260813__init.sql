-- V20260813__init.sql
-- 建材管理系统初始化 Schema（MySQL 8 / utf8mb4）
-- Docker: MYSQL_DATABASE=cailu_bom 时仍可安全执行；本地手动执行时会创建库。

CREATE DATABASE IF NOT EXISTS cailu_bom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cailu_bom;

-- ========== system ==========
CREATE TABLE IF NOT EXISTS sys_org (
    id            BIGINT       NOT NULL PRIMARY KEY,
    parent_id     BIGINT       NULL DEFAULT 0,
    name          VARCHAR(128) NOT NULL,
    code          VARCHAR(64)  NULL,
    sort_no       INT          NOT NULL DEFAULT 0,
    status        TINYINT      NOT NULL DEFAULT 1,
    created_at    DATETIME     NULL,
    updated_at    DATETIME     NULL,
    created_by    BIGINT       NULL,
    updated_by    BIGINT       NULL,
    deleted       TINYINT      NOT NULL DEFAULT 0,
    KEY idx_sys_org_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user (
    id             BIGINT       NOT NULL PRIMARY KEY,
    username       VARCHAR(64)  NOT NULL,
    password_hash  VARCHAR(128) NOT NULL,
    real_name      VARCHAR(64)  NULL,
    mobile         VARCHAR(32)  NULL,
    org_id         BIGINT       NULL,
    status         TINYINT      NOT NULL DEFAULT 1,
    created_at     DATETIME     NULL,
    updated_at     DATETIME     NULL,
    created_by     BIGINT       NULL,
    updated_by     BIGINT       NULL,
    deleted        TINYINT      NOT NULL DEFAULT 0,
    UNIQUE KEY uk_sys_user_username (username),
    KEY idx_sys_user_org (org_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
    id         BIGINT       NOT NULL PRIMARY KEY,
    code       VARCHAR(64)  NOT NULL,
    name       VARCHAR(64)  NOT NULL,
    status     TINYINT      NOT NULL DEFAULT 1,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    created_by BIGINT       NULL,
    updated_by BIGINT       NULL,
    deleted    TINYINT      NOT NULL DEFAULT 0,
    UNIQUE KEY uk_sys_role_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
    id      BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE KEY uk_sys_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== erp 进销存 ==========
CREATE TABLE IF NOT EXISTS erp_warehouse (
    id         BIGINT       NOT NULL PRIMARY KEY,
    code       VARCHAR(64)  NOT NULL,
    name       VARCHAR(128) NOT NULL,
    address    VARCHAR(255) NULL,
    status     TINYINT      NOT NULL DEFAULT 1,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    created_by BIGINT       NULL,
    updated_by BIGINT       NULL,
    deleted    TINYINT      NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_warehouse_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_product (
    id             BIGINT         NOT NULL PRIMARY KEY,
    sku            VARCHAR(64)    NOT NULL,
    name           VARCHAR(128)   NOT NULL,
    category       VARCHAR(64)    NULL,
    unit           VARCHAR(16)    NULL,
    spec           VARCHAR(128)   NULL,
    purchase_price DECIMAL(18, 4) NOT NULL DEFAULT 0,
    sale_price     DECIMAL(18, 4) NOT NULL DEFAULT 0,
    status         TINYINT        NOT NULL DEFAULT 1,
    created_at     DATETIME       NULL,
    updated_at     DATETIME       NULL,
    created_by     BIGINT         NULL,
    updated_by     BIGINT         NULL,
    deleted        TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_product_sku (sku),
    KEY idx_erp_product_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_partner (
    id         BIGINT       NOT NULL PRIMARY KEY,
    type       VARCHAR(16)  NOT NULL COMMENT 'supplier/customer/both',
    code       VARCHAR(64)  NOT NULL,
    name       VARCHAR(128) NOT NULL,
    contact    VARCHAR(64)  NULL,
    mobile     VARCHAR(32)  NULL,
    status     TINYINT      NOT NULL DEFAULT 1,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    created_by BIGINT       NULL,
    updated_by BIGINT       NULL,
    deleted    TINYINT      NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_partner_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_purchase_order (
    id            BIGINT         NOT NULL PRIMARY KEY,
    order_no      VARCHAR(64)    NOT NULL,
    supplier_id   BIGINT         NOT NULL,
    warehouse_id  BIGINT         NOT NULL,
    status        VARCHAR(32)    NOT NULL DEFAULT 'draft',
    total_amount  DECIMAL(18, 4) NOT NULL DEFAULT 0,
    remark        VARCHAR(255)   NULL,
    created_at    DATETIME       NULL,
    updated_at    DATETIME       NULL,
    created_by    BIGINT         NULL,
    updated_by    BIGINT         NULL,
    deleted       TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_po_no (order_no),
    KEY idx_erp_po_supplier (supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_purchase_order_item (
    id          BIGINT         NOT NULL PRIMARY KEY,
    order_id    BIGINT         NOT NULL,
    product_id  BIGINT         NOT NULL,
    qty         DECIMAL(18, 4) NOT NULL,
    price       DECIMAL(18, 4) NOT NULL,
    amount      DECIMAL(18, 4) NOT NULL,
    KEY idx_erp_poi_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_sales_order (
    id            BIGINT         NOT NULL PRIMARY KEY,
    order_no      VARCHAR(64)    NOT NULL,
    customer_id   BIGINT         NOT NULL,
    warehouse_id  BIGINT         NOT NULL,
    status        VARCHAR(32)    NOT NULL DEFAULT 'draft',
    total_amount  DECIMAL(18, 4) NOT NULL DEFAULT 0,
    remark        VARCHAR(255)   NULL,
    created_at    DATETIME       NULL,
    updated_at    DATETIME       NULL,
    created_by    BIGINT         NULL,
    updated_by    BIGINT         NULL,
    deleted       TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_so_no (order_no),
    KEY idx_erp_so_customer (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_sales_order_item (
    id          BIGINT         NOT NULL PRIMARY KEY,
    order_id    BIGINT         NOT NULL,
    product_id  BIGINT         NOT NULL,
    qty         DECIMAL(18, 4) NOT NULL,
    price       DECIMAL(18, 4) NOT NULL,
    amount      DECIMAL(18, 4) NOT NULL,
    KEY idx_erp_soi_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_stock (
    id            BIGINT         NOT NULL PRIMARY KEY,
    warehouse_id  BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    quantity      DECIMAL(18, 4) NOT NULL DEFAULT 0,
    locked_qty    DECIMAL(18, 4) NOT NULL DEFAULT 0,
    created_at    DATETIME       NULL,
    updated_at    DATETIME       NULL,
    created_by    BIGINT         NULL,
    updated_by    BIGINT         NULL,
    deleted       TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_stock_wh_prod (warehouse_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_stock_ledger (
    id            BIGINT         NOT NULL PRIMARY KEY,
    warehouse_id  BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    biz_type      VARCHAR(32)    NOT NULL,
    biz_id        BIGINT         NULL,
    change_qty    DECIMAL(18, 4) NOT NULL,
    balance_qty   DECIMAL(18, 4) NOT NULL,
    remark        VARCHAR(255)   NULL,
    created_at    DATETIME       NULL,
    created_by    BIGINT         NULL,
    KEY idx_erp_ledger_prod (product_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== oa 微OA ==========
CREATE TABLE IF NOT EXISTS oa_approval (
    id           BIGINT       NOT NULL PRIMARY KEY,
    title        VARCHAR(128) NOT NULL,
    biz_type     VARCHAR(32)  NULL,
    biz_id       BIGINT       NULL,
    applicant_id BIGINT       NOT NULL,
    status       VARCHAR(32)  NOT NULL DEFAULT 'draft',
    remark       VARCHAR(255) NULL,
    created_at   DATETIME     NULL,
    updated_at   DATETIME     NULL,
    created_by   BIGINT       NULL,
    updated_by   BIGINT       NULL,
    deleted      TINYINT      NOT NULL DEFAULT 0,
    KEY idx_oa_approval_applicant (applicant_id),
    KEY idx_oa_approval_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS oa_approval_task (
    id           BIGINT       NOT NULL PRIMARY KEY,
    approval_id  BIGINT       NOT NULL,
    assignee_id  BIGINT       NOT NULL,
    node_name    VARCHAR(64)  NULL,
    status       VARCHAR(32)  NOT NULL DEFAULT 'pending',
    comment      VARCHAR(255) NULL,
    acted_at     DATETIME     NULL,
    created_at   DATETIME     NULL,
    KEY idx_oa_task_assignee (assignee_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS oa_notice (
    id         BIGINT       NOT NULL PRIMARY KEY,
    title      VARCHAR(128) NOT NULL,
    content    TEXT         NULL,
    status     TINYINT      NOT NULL DEFAULT 1,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    created_by BIGINT       NULL,
    updated_by BIGINT       NULL,
    deleted    TINYINT      NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== finance 财务 ==========
CREATE TABLE IF NOT EXISTS fin_receivable (
    id               BIGINT         NOT NULL PRIMARY KEY,
    bill_no          VARCHAR(64)    NOT NULL,
    customer_id      BIGINT         NOT NULL,
    source_order_id  BIGINT         NULL,
    amount           DECIMAL(18, 4) NOT NULL,
    received_amount  DECIMAL(18, 4) NOT NULL DEFAULT 0,
    due_date         DATE           NULL,
    status           VARCHAR(32)    NOT NULL DEFAULT 'open',
    created_at       DATETIME       NULL,
    updated_at       DATETIME       NULL,
    created_by       BIGINT         NULL,
    updated_by       BIGINT         NULL,
    deleted          TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_fin_ar_bill (bill_no),
    KEY idx_fin_ar_customer (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fin_payable (
    id               BIGINT         NOT NULL PRIMARY KEY,
    bill_no          VARCHAR(64)    NOT NULL,
    supplier_id      BIGINT         NOT NULL,
    source_order_id  BIGINT         NULL,
    amount           DECIMAL(18, 4) NOT NULL,
    paid_amount      DECIMAL(18, 4) NOT NULL DEFAULT 0,
    due_date         DATE           NULL,
    status           VARCHAR(32)    NOT NULL DEFAULT 'open',
    created_at       DATETIME       NULL,
    updated_at       DATETIME       NULL,
    created_by       BIGINT         NULL,
    updated_by       BIGINT         NULL,
    deleted          TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_fin_ap_bill (bill_no),
    KEY idx_fin_ap_supplier (supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fin_payment (
    id           BIGINT         NOT NULL PRIMARY KEY,
    payment_no   VARCHAR(64)    NOT NULL,
    direction    VARCHAR(16)    NOT NULL COMMENT 'in/out',
    partner_id   BIGINT         NOT NULL,
    amount       DECIMAL(18, 4) NOT NULL,
    related_bill VARCHAR(64)    NULL,
    paid_at      DATETIME       NULL,
    remark       VARCHAR(255)   NULL,
    created_at   DATETIME       NULL,
    updated_at   DATETIME       NULL,
    created_by   BIGINT         NULL,
    updated_by   BIGINT         NULL,
    deleted      TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_fin_payment_no (payment_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_outbound_order (
    id              BIGINT         NOT NULL PRIMARY KEY,
    bill_no         VARCHAR(64)    NOT NULL,
    custom_no       VARCHAR(64)    NULL,
    bill_date       DATE           NOT NULL,
    outbound_type   VARCHAR(32)    NOT NULL,
    order_no        VARCHAR(64)    NULL,
    order_custom_no VARCHAR(64)    NULL,
    order_date      DATE           NULL,
    order_type      VARCHAR(32)    NULL,
    customer_code   VARCHAR(64)    NOT NULL,
    customer_name   VARCHAR(128)   NOT NULL,
    contact         VARCHAR(64)    NULL,
    mobile          VARCHAR(32)    NULL,
    community       VARCHAR(128)   NULL,
    address         VARCHAR(255)   NOT NULL,
    channel         VARCHAR(64)    NULL,
    customer_level  VARCHAR(32)    NULL,
    sales_dept      VARCHAR(64)    NOT NULL,
    salesperson     VARCHAR(64)    NULL,
    guide           VARCHAR(64)    NULL,
    designer        VARCHAR(64)    NULL,
    design_method   VARCHAR(64)    NULL,
    ship_from       VARCHAR(64)    NULL,
    delivery_method VARCHAR(64)    NULL,
    package_product VARCHAR(128)   NULL,
    handler         VARCHAR(64)    NULL,
    inner_remark    VARCHAR(255)   NULL,
    outer_remark    VARCHAR(255)   NULL,
    status          VARCHAR(32)    NOT NULL DEFAULT 'draft',
    total_amount    DECIMAL(18, 4) NOT NULL DEFAULT 0,
    total_qty       DECIMAL(18, 4) NOT NULL DEFAULT 0,
    total_boxes     DECIMAL(18, 4) NOT NULL DEFAULT 0,
    total_sqm       DECIMAL(18, 4) NOT NULL DEFAULT 0,
    total_weight    DECIMAL(18, 4) NOT NULL DEFAULT 0,
    created_at      DATETIME       NULL,
    updated_at      DATETIME       NULL,
    created_by      BIGINT         NULL,
    updated_by      BIGINT         NULL,
    deleted         TINYINT        NOT NULL DEFAULT 0,
    UNIQUE KEY uk_erp_outbound_bill (bill_no),
    KEY idx_erp_outbound_customer (customer_code),
    KEY idx_erp_outbound_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS erp_outbound_order_item (
    id              BIGINT         NOT NULL PRIMARY KEY,
    order_id        BIGINT         NOT NULL,
    line_no         INT            NOT NULL DEFAULT 1,
    position        VARCHAR(64)    NULL,
    product_id      BIGINT         NULL,
    sku             VARCHAR(128)   NOT NULL,
    product_name    VARCHAR(128)   NULL,
    brand           VARCHAR(64)    NULL,
    spec            VARCHAR(128)   NULL,
    grade           VARCHAR(32)    NULL,
    batch_no        VARCHAR(64)    NULL,
    sets_qty        DECIMAL(18, 4) NOT NULL DEFAULT 0,
    boxes           DECIMAL(18, 4) NOT NULL DEFAULT 0,
    pieces          DECIMAL(18, 4) NOT NULL DEFAULT 0,
    qty             DECIMAL(18, 4) NOT NULL,
    unit            VARCHAR(16)    NULL,
    std_price       DECIMAL(18, 4) NOT NULL DEFAULT 0,
    discount        DECIMAL(18, 4) NOT NULL DEFAULT 0,
    price           DECIMAL(18, 4) NOT NULL DEFAULT 0,
    amount          DECIMAL(18, 4) NOT NULL DEFAULT 0,
    warehouse_id    BIGINT         NULL,
    warehouse_code  VARCHAR(64)    NULL,
    warehouse_name  VARCHAR(128)   NULL,
    bin_name        VARCHAR(64)    NULL,
    fee_type        VARCHAR(64)    NULL,
    occupy          TINYINT        NOT NULL DEFAULT 0,
    count_perf      TINYINT        NOT NULL DEFAULT 1,
    KEY idx_erp_outbound_item_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
