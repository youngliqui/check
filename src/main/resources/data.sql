CREATE TABLE public.product
(
    id                BIGSERIAL PRIMARY KEY,
    description       VARCHAR(50)    NOT NULL,
    price             DECIMAL(10, 2) NOT NULL CHECK (price >= 0.00),
    quantity_in_stock INTEGER        NOT NULL CHECK (quantity_in_stock >= 0),
    wholesale_product BOOLEAN        NOT NULL
);

CREATE TABLE public.discount_card
(
    id     BIGSERIAL PRIMARY KEY,
    number INTEGER  NOT NULL UNIQUE,
    amount SMALLINT NOT NULL CHECK (0 <= amount AND amount <= 100)
);

INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product)
VALUES (1, 'Milk', 1.07, 10, true),
       (2, 'Cream 400g', 2.71, 20, true),
       (3, 'Yogurt 400g', 2.10, 7, true),
       (4, 'Packed potatoes 1 kg', 1.47, 30, false),
       (5, 'Packed cabbage 1kg', 1.19, 15, false),
       (6, 'Packed tomatoes 350g', 1.60, 50, false),
       (7, 'Packed apples 1kg', 2.78, 18, false),
       (8, 'Packed oranges 1kg', 3.20, 12, false),
       (9, 'Packed bananas 1kg', 1.10, 25, true),
       (10, 'Packed beef fillet 1kg', 12.80, 7, false),
       (11, 'Packed pork filled 1kg', 8.52, 14, false),
       (12, 'Packed chicken breasts 1 kgSour', 10.75, 18, false),
       (13, 'Baguette 360g', 1.30, 10, true),
       (14, 'Drinking water 1,5l', 0.80, 100, false),
       (15, 'Olive oil 500 ml', 5.30, 16, false),
       (16, 'Sunflower oil 1l', 1.20, 12, false),
       (17, 'Chocolate Ritter sport 100 g', 1.10, 50, true),
       (18, 'Paulaner 0,5l', 1.10, 100, false),
       (19, 'Whiskey Jim Beam 1l', 13.99, 30, false),
       (20, 'Whiskey Jack Daniels 1l', 17.19, 20, false);

INSERT INTO public.discount_card (id, number, amount)
VALUES (1, '1111', 3),
       (2, '2222', 3),
       (3, '3333', 4),
       (4, '4444', 5);