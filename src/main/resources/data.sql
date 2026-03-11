-- Products
INSERT INTO PRODUCTS (name, description, price, created_at, updated_at) VALUES
('iPhone 15', 'Latest Apple smartphone', 999.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Samsung Galaxy S24', 'Latest Samsung smartphone', 899.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('MacBook Pro 16"', 'Apple laptop', 2499.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Dell XPS 13', 'Dell ultrabook', 1299.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('AirPods Pro', 'Apple wireless earbuds', 249.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('iPad Air', 'Apple tablet', 749.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Seed sales (for recommendation engine)
INSERT INTO SALES (created_at) VALUES
(CURRENT_TIMESTAMP()),
(CURRENT_TIMESTAMP()),
(CURRENT_TIMESTAMP());

-- Sale 1: iPhone 15 + AirPods Pro
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 999.99),
(1, 5, 1, 249.99);

-- Sale 2: MacBook Pro + iPad Air
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
(2, 3, 1, 2499.99),
(2, 6, 1, 749.99);

-- Sale 3: iPhone 15 x2 + MacBook Pro + AirPods Pro
INSERT INTO SALE_ITEMS (sale_id, product_id, quantity, unit_price) VALUES
(3, 1, 2, 999.99),
(3, 3, 1, 2499.99),
(3, 5, 1, 249.99);
