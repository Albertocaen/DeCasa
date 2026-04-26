-- =============================================================
-- DE CASA — Schema (MySQL 8)
-- =============================================================

CREATE DATABASE IF NOT EXISTS decasa
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
SET NAMES utf8mb4;

USE decasa;

CREATE TABLE IF NOT EXISTS category (
                                        id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        name         VARCHAR(50)  NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    sort_order   INT          NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS product (
                                       id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       category_id   BIGINT       NOT NULL,
                                       name          VARCHAR(150) NOT NULL,
    description   TEXT,
    base_price    DECIMAL(8,2) NOT NULL,
    minimum_units INT          NOT NULL DEFAULT 1,
    available     BOOLEAN      NOT NULL DEFAULT TRUE,
    exclusive     BOOLEAN      NOT NULL DEFAULT FALSE,
    image_url     VARCHAR(500),
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES category(id)
    );

CREATE INDEX idx_product_category  ON product(category_id);
CREATE INDEX idx_product_available ON product(available);

CREATE TABLE IF NOT EXISTS weekly_special (
                                              id         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                              product_id BIGINT      NOT NULL,
                                              type       VARCHAR(10) NOT NULL,
    week_of    DATE        NOT NULL,
    active     BOOLEAN     NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_weekly_product
    FOREIGN KEY (product_id) REFERENCES product(id),
    UNIQUE KEY uq_weekly_type_week (type, week_of)
    );

CREATE INDEX idx_weekly_active ON weekly_special(active, week_of);

CREATE TABLE IF NOT EXISTS pack (
                                    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    name        VARCHAR(100) NOT NULL,
    description TEXT,
    price_min   DECIMAL(8,2) NOT NULL,
    price_max   DECIMAL(8,2),
    available   BOOLEAN      NOT NULL DEFAULT TRUE,
    sort_order  INT          NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS pack_component (
                                              id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                              pack_id     BIGINT       NOT NULL,
                                              product_id  BIGINT,
                                              quantity    INT          NOT NULL,
                                              description VARCHAR(200),
    CONSTRAINT fk_pc_pack
    FOREIGN KEY (pack_id) REFERENCES pack(id) ON DELETE CASCADE,
    CONSTRAINT fk_pc_product
    FOREIGN KEY (product_id) REFERENCES product(id)
    );

CREATE INDEX idx_pack_component_pack    ON pack_component(pack_id);
CREATE INDEX idx_pack_component_product ON pack_component(product_id);

-- Uso customer_orders para evitar conflictos con ORDER/ORDERS
CREATE TABLE IF NOT EXISTS customer_orders (
                                               id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                               order_number     VARCHAR(20) NOT NULL UNIQUE,
    customer_name    VARCHAR(150) NOT NULL,
    customer_email   VARCHAR(200) NOT NULL,
    customer_phone   VARCHAR(30) NOT NULL,
    delivery_type    VARCHAR(10) NOT NULL,
    delivery_address TEXT,
    delivery_date    DATETIME NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes            TEXT,
    subtotal         DECIMAL(10,2) NOT NULL,
    delivery_fee     DECIMAL(8,2) NOT NULL DEFAULT 0,
    total            DECIMAL(10,2) NOT NULL,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
    );

CREATE INDEX idx_orders_status
    ON customer_orders(status);

CREATE INDEX idx_orders_delivery_date
    ON customer_orders(delivery_date);

CREATE INDEX idx_orders_email
    ON customer_orders(customer_email);