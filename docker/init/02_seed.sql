-- =============================================================
-- DE CASA — Datos iniciales
-- =============================================================

USE decasa;
SET NAMES 'utf8mb4';

INSERT INTO category (name, display_name, sort_order) VALUES
    ('TAPAS_CLASICAS', 'Tapas Clásicas',       1),
    ('TAPAS_PREMIUM',  'Tapas Premium',         2),
    ('COCINA',         'De Nuestra Cocina',     3),
    ('HORNO',          'De Nuestro Horno',      4),
    ('COMPARTIR',      'Para Compartir',        5),
    ('POSTRES',        'Postres',               6),
    ('ENCARGOS',       'Encargos Especiales',   7);

-- Tapas Clásicas
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Pintxo de tortilla de Richi',          'La tortilla de Richi en su versión tapa', 3.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Champiñón relleno de queso y bacon',   NULL,                                      3.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Croqueta de jamón',                    'Bechamel casera, jamón serrano',           3.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Tosta de nugget de pollo con queso',   NULL,                                      3.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Tosta de huevo de codorniz con chistorra', NULL,                                  3.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_CLASICAS'), 'Tosta de ensaladilla rusa',            'Ensaladilla casera de Richi',             3.00, 6);

-- Tapas Premium
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'TAPAS_PREMIUM'), 'Tosta de jamón serrano con tomate',          NULL, 4.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_PREMIUM'), 'Tosta de huevo duro con langostino',         NULL, 4.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_PREMIUM'), 'Tosta de solomillo con queso azul',          NULL, 4.00, 6),
    ((SELECT id FROM category WHERE name = 'TAPAS_PREMIUM'), 'Piruleta sorpresa',                          'Sorpresa de Richi', 3.50, 6);

INSERT INTO product (category_id, name, description, base_price, minimum_units, exclusive) VALUES
    ((SELECT id FROM category WHERE name = 'TAPAS_PREMIUM'), 'Steak tartar sobre galleta', 'Solo por encargo', 4.00, 6, TRUE);

-- De Nuestra Cocina
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'COCINA'), 'Tortilla de Richi 24cm',    'Entera · con o sin cebolla',          25.00, 1),
    ((SELECT id FROM category WHERE name = 'COCINA'), 'Tortillón de Richi 28cm',   'Entera · con o sin cebolla',          30.00, 1),
    ((SELECT id FROM category WHERE name = 'COCINA'), 'Croquetas de jamón (6 uds)', 'Bechamel casera',                    2.00,  6),
    ((SELECT id FROM category WHERE name = 'COCINA'), 'Croquetas de jamón (12 uds)', 'Pack de 12 — mejor precio',         1.83,  12);

-- De Nuestro Horno
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'HORNO'), 'Mini burger artesana de ternera', 'Pan casero + ternera + queso',     3.00, 6),
    ((SELECT id FROM category WHERE name = 'HORNO'), 'Mini burger de pollo',             'Pan casero + pollo + salsa casera', 3.00, 6);

-- Para Compartir
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'COMPARTIR'), 'Tabla española pequeña', 'Jamón · chorizo · lomo · manchego · 2-4 personas', 22.00, 1),
    ((SELECT id FROM category WHERE name = 'COMPARTIR'), 'Tabla española grande',  'Jamón · chorizo · lomo · manchego · 6-8 personas', 38.00, 1),
    ((SELECT id FROM category WHERE name = 'COMPARTIR'), 'Hummus casero de Richi', 'Con pan artesano + crudités',                      12.00, 1),
    ((SELECT id FROM category WHERE name = 'COMPARTIR'), 'Ensalada fresca',        'De temporada · Richi decide',                      14.00, 1);

-- Postres
INSERT INTO product (category_id, name, description, base_price, minimum_units) VALUES
    ((SELECT id FROM category WHERE name = 'POSTRES'), 'Buñuelos de Richi',           NULL,           3.00, 6),
    ((SELECT id FROM category WHERE name = 'POSTRES'), 'Natillas caseras en vasito',  NULL,           3.00, 6),
    ((SELECT id FROM category WHERE name = 'POSTRES'), 'Arroz con leche en vasito',   NULL,           3.00, 6),
    ((SELECT id FROM category WHERE name = 'POSTRES'), 'Tarta de queso con fresa',    '6-8 personas', 22.00, 1),
    ((SELECT id FROM category WHERE name = 'POSTRES'), 'Tarta de la abuela',          '6-8 personas', 18.00, 1);

-- Encargos (exclusivos)
INSERT INTO product (category_id, name, description, base_price, minimum_units, exclusive) VALUES
    ((SELECT id FROM category WHERE name = 'ENCARGOS'), 'Steak tartar entero', 'Solo por WhatsApp · 48-72h antelación', 40.00, 1, TRUE),
    ((SELECT id FROM category WHERE name = 'ENCARGOS'), 'Rabo de toro',        'Guiso tradicional · solo por encargo',  40.00, 1, TRUE),
    ((SELECT id FROM category WHERE name = 'ENCARGOS'), 'Arroces',             'Arroz del día · solo por encargo',      40.00, 1, TRUE);

-- Packs
INSERT INTO pack (name, description, price_min, price_max, sort_order) VALUES
    ('Fiesta',      '12 croquetas + 18 tapas a elección del chef. Para 6-10 personas.',          48.00,  58.00,  1),
    ('Celebración', 'Tortilla 24cm + 12 croquetas + 18 tapas. Para 8-12 personas.',              68.00,  88.00,  2),
    ('Gran Fiesta', 'Tortilla 24cm + 18 croquetas + 24 tapas. Para 12-20 personas.',             118.00, 128.00, 3),
    ('XL',          'Tortilla 24cm + 24 croquetas + 50 tapas + 24 mini burgers. Para 20-30 personas.', 200.00, NULL, 4);

-- Componentes del pack Fiesta
INSERT INTO pack_component (pack_id, product_id, quantity, description) VALUES
    ((SELECT id FROM pack WHERE name = 'Fiesta'), (SELECT id FROM product WHERE name = 'Croquetas de jamón (6 uds)'), 12, 'Croquetas de jamón'),
    ((SELECT id FROM pack WHERE name = 'Fiesta'), NULL, 18, 'Tapas a elección del chef');

-- Componentes del pack Celebración
INSERT INTO pack_component (pack_id, product_id, quantity, description) VALUES
    ((SELECT id FROM pack WHERE name = 'Celebración'), (SELECT id FROM product WHERE name = 'Tortilla de Richi 24cm'),    1,  'Tortilla de Richi 24cm'),
    ((SELECT id FROM pack WHERE name = 'Celebración'), (SELECT id FROM product WHERE name = 'Croquetas de jamón (6 uds)'), 12, 'Croquetas de jamón'),
    ((SELECT id FROM pack WHERE name = 'Celebración'), NULL, 18, 'Tapas a elección del chef');

-- Componentes del pack Gran Fiesta
INSERT INTO pack_component (pack_id, product_id, quantity, description) VALUES
    ((SELECT id FROM pack WHERE name = 'Gran Fiesta'), (SELECT id FROM product WHERE name = 'Tortilla de Richi 24cm'),    1,  'Tortilla de Richi 24cm'),
    ((SELECT id FROM pack WHERE name = 'Gran Fiesta'), (SELECT id FROM product WHERE name = 'Croquetas de jamón (6 uds)'), 18, 'Croquetas de jamón'),
    ((SELECT id FROM pack WHERE name = 'Gran Fiesta'), NULL, 24, 'Tapas a elección del chef');

-- Componentes del pack XL
INSERT INTO pack_component (pack_id, product_id, quantity, description) VALUES
    ((SELECT id FROM pack WHERE name = 'XL'), (SELECT id FROM product WHERE name = 'Tortilla de Richi 24cm'),         1,  'Tortilla de Richi 24cm'),
    ((SELECT id FROM pack WHERE name = 'XL'), (SELECT id FROM product WHERE name = 'Croquetas de jamón (6 uds)'),     24, 'Croquetas de jamón'),
    ((SELECT id FROM pack WHERE name = 'XL'), (SELECT id FROM product WHERE name = 'Mini burger artesana de ternera'), 24, 'Mini burgers artesanas'),
    ((SELECT id FROM pack WHERE name = 'XL'), NULL, 50, 'Tapas a elección del chef');
