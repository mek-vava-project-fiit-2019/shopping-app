--Inserting FOOD products
INSERT INTO product(id, category, name, picture) VALUES (1, 'food', 'Potatoes', null);
INSERT INTO product(id, category, name, picture) VALUES (2, 'food', 'Apple', null);
INSERT INTO product(id, category, name, picture) VALUES (3, 'food', 'Banana', null);
INSERT INTO product(id, category, name, picture) VALUES (4, 'food', 'Youghurt', null);
INSERT INTO product(id, category, name, picture) VALUES (5, 'food', 'Cheese', null);
INSERT INTO product(id, category, name, picture) VALUES (6, 'food', 'Tomato', null);
INSERT INTO product(id, category, name, picture) VALUES (7, 'food', 'Carrot', null);
INSERT INTO product(id, category, name, picture) VALUES (8, 'food', 'Chicken', null);
INSERT INTO product(id, category, name, picture) VALUES (9, 'food', 'Water', null);
INSERT INTO product(id, category, name, picture) VALUES (10, 'food', 'Milk', null);

--Inserting CLOTHES products
INSERT INTO product(id, category, name, picture) VALUES (11, 'clothes', 'T-shirt', null);
INSERT INTO product(id, category, name, picture) VALUES (12, 'clothes', 'Trousers', null);
INSERT INTO product(id, category, name, picture) VALUES (13, 'clothes', 'Sweatshirt', null);
INSERT INTO product(id, category, name, picture) VALUES (14, 'clothes', 'Bandanna', null);
INSERT INTO product(id, category, name, picture) VALUES (15, 'clothes', 'Jacket', null);
INSERT INTO product(id, category, name, picture) VALUES (16, 'clothes', 'Fedora', null);
INSERT INTO product(id, category, name, picture) VALUES (17, 'clothes', 'Sneakers', null);
INSERT INTO product(id, category, name, picture) VALUES (18, 'clothes', 'Boots', null);
INSERT INTO product(id, category, name, picture) VALUES (19, 'clothes', 'Socks', null);
INSERT INTO product(id, category, name, picture) VALUES (20, 'clothes', 'Slippers', null);

--Inserting ELECTRONICS products
INSERT INTO product(id, category, name, picture) VALUES (21, 'electronics', 'Smarthpone', null);
INSERT INTO product(id, category, name, picture) VALUES (22, 'electronics', 'Television', null);
INSERT INTO product(id, category, name, picture) VALUES (23, 'electronics', 'Microwave', null);
INSERT INTO product(id, category, name, picture) VALUES (24, 'electronics', 'Refrigerator', null);
INSERT INTO product(id, category, name, picture) VALUES (25, 'electronics', 'Washer', null);
INSERT INTO product(id, category, name, picture) VALUES (26, 'electronics', 'Toaster', null);
INSERT INTO product(id, category, name, picture) VALUES (27, 'electronics', 'Computer', null);
INSERT INTO product(id, category, name, picture) VALUES (28, 'electronics', 'Laptop', null);
INSERT INTO product(id, category, name, picture) VALUES (29, 'electronics', 'Tablet', null);
INSERT INTO product(id, category, name, picture) VALUES (30, 'electronics', 'Game console', null);

--Inserting customers
INSERT INTO customer(id, email, name, password) VALUES (1, '111@shop.com', 'Milos Zeman', '1111111');
INSERT INTO customer(id, email, name, password) VALUES (2, '222@shop.com', 'James Bond', '2222222');
INSERT INTO customer(id, email, name, password) VALUES (3, '333@shop.com', 'Robert Fico', '3333333');
INSERT INTO customer(id, email, name, password) VALUES (4, '444@shop.com', 'Peter Marcin', '4444444');
INSERT INTO customer(id, email, name, password) VALUES (5, '555@shop.com', 'Milan Knazko', '5555555');

--Inserting sortiment
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (1 ,34,5,1,1);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (2 ,76,5,3,1);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (3 ,124,5,5,1);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (4 ,325,5,13,1);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (5 ,745,5,18,1);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (6 ,436,10,2,2);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (7 ,875,10,8,2);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (8 ,12,10,4,2);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (9 ,345,10,6,2);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (10,97,10,7,2);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (11,345,15,9,3);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (12,645,15,10,3);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (13,345,15,1,3);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (14,64,15,2,3);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (15,765,15,15,3);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (16,987,20,22,4);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (17,234,15,24,4);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (18,46,30,30,4);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (19,346,50,28,4);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (20,345,60,21,4);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (21,765,356,11,5);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (22,345,560,14,5);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (23,765,220,16,5);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (24,343,115,19,5);
INSERT INTO sortiment(id, amount, price, product_id, shop_id) VALUES (25,876,678,17,5);