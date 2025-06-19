DELETE FROM favorites; -- TRUNCATE TABLE favorits;
DELETE FROM products;-- TRUNCATE TABLE products;
DELETE FROM shop_users;-- TRUNCATE TABLE shop_user;


INSERT INTO shop_users(name, email) values ('Alex', 'alex@gmail.com');

INSERT INTO products(name, description, discount_price, price) values ('lopata', 'super lopate', 0, 0);