TRUNCATE TABLE
    cart_items,
    cart,
    order_items,
    payments,
    orders,
    favorites,
    products,
    categories,
    shop_users
    RESTART IDENTITY CASCADE;

INSERT INTO categories (name)
VALUES ('Smartphones');
INSERT INTO categories (name)
VALUES ('Laptops');
INSERT INTO categories (name)
VALUES ('Headphones');
INSERT INTO categories (name)
VALUES ('Tablets');
INSERT INTO categories (name)
VALUES ('Smartwatches');
INSERT INTO categories (name)
VALUES ('Monitors');

INSERT INTO products (name, description, price, discount_price, image_url, category_id)
VALUES ('iPhone 15 Pro',
        'Apple flagship smartphone with A17 Pro chip and titanium frame.',
        1399.99,
        1299.99,
        'https://example.com/images/iphone15pro.jpg',
        1);

INSERT INTO products (name, description, price, discount_price, image_url, category_id)
VALUES ('MacBook Air M2',
        'Lightweight and powerful laptop featuring Apple M2 chip and all-day battery.',
        1199.00,
        NULL,
        'https://example.com/images/macbook_air_m2.jpg',
        2);

INSERT INTO products (name, description, price, discount_price, image_url, category_id)
VALUES ('Bose QuietComfort Ultra',
        'Premium noise-cancelling headphones with outstanding comfort and audio quality.',
        399.99,
        349.99,
        'https://example.com/images/bose_qc_ultra.jpg',
        3);

INSERT INTO products (name, description, price, discount_price, image_url, category_id)
VALUES ('Asus ROG Zephyrus G14',
        'Compact gaming laptop powered by AMD Ryzen and NVIDIA RTX graphics.',
        1599.99,
        1449.99,
        'https://example.com/images/rog_zephyrus_g14.jpg',
        2);

INSERT INTO products (name, description, price, discount_price, image_url, category_id)
VALUES ('Samsung Galaxy Z Flip5',
        'Foldable smartphone with a sleek design and flexible display.',
        999.00,
        949.00,
        'https://example.com/images/galaxy_z_flip5.jpg',
        1);

