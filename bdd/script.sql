-- ============================================================
-- ShopWise - Script SQL complet (schéma + données initiales)
-- Compatible : H2, MySQL, MariaDB (avec légères adaptations)
-- ============================================================

-- Table PRODUCTS (existante, complétée)
CREATE TABLE IF NOT EXISTS PRODUCTS (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(500),
    price       DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table SALES - en-tête de vente
CREATE TABLE IF NOT EXISTS SALES (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table SALE_ITEMS - lignes de vente
CREATE TABLE IF NOT EXISTS SALE_ITEMS (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id    BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INT            NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_sale_items_sale    FOREIGN KEY (sale_id)    REFERENCES SALES(id)    ON DELETE CASCADE,
    CONSTRAINT fk_sale_items_product FOREIGN KEY (product_id) REFERENCES PRODUCTS(id) ON DELETE RESTRICT
);

-- Index pour les requêtes de recommandation (performances)
CREATE INDEX IF NOT EXISTS idx_sale_items_product_id ON SALE_ITEMS(product_id);
CREATE INDEX IF NOT EXISTS idx_sale_items_sale_id    ON SALE_ITEMS(sale_id);

-- ============================================================
-- Données initiales - Produits
-- ============================================================
INSERT INTO PRODUCTS (name, description, price) VALUES
    ('iPhone 15',          'Latest Apple smartphone',  999.99),
    ('Samsung Galaxy S24', 'Latest Samsung smartphone', 899.99),
    ('MacBook Pro 16"',    'Apple laptop',             2499.99),
    ('Dell XPS 13',        'Dell ultrabook',           1299.99),
    ('AirPods Pro',        'Apple wireless earbuds',    249.99),
    ('iPad Air',           'Apple tablet',              749.99);

-- ============================================================
-- Données initiales - Ventes (pour alimenter les recommandations)
-- ============================================================
INSERT INTO SALES (created_at) VALUES
    (CURRENT_TIMESTAMP),
    (CURRENT_TIMESTAMP),
    (CURRENT_TIMESTAMP);

-- Vente 1 : iPhone 15 + AirPods Pro (achetés ensemble)
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
    (1, 1, 1, 999.99),
    (1, 5, 1, 249.99);

-- Vente 2 : MacBook Pro + iPad Air
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
    (2, 3, 1, 2499.99),
    (2, 6, 1, 749.99);

-- Vente 3 : iPhone 15 + MacBook Pro + AirPods Pro
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
    (3, 1, 2, 999.99),
    (3, 3, 1, 2499.99),
    (3, 5, 1, 249.99);
