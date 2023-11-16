INSERT INTO tienda (id, ciudad, direccion, telefono) VALUES ('7e9152bb-8e1f-472f-9fe0-0e5349483adf', 'Madrid', 'C Tia Javiera N11', '654453423');
INSERT INTO tienda (id, ciudad, direccion, telefono) VALUES ('8e9152bb-8e1f-472f-9fe0-0e5349483adf', 'Leganes', 'C Navarra', '664453423');
INSERT INTO personal (id, nombre, apellido, cargo, tienda_id) VALUES (RANDOM_UUID(), 'Luis', 'Ramos', 1, '7e9152bb-8e1f-472f-9fe0-0e5349483adf');
INSERT INTO personal (id, nombre, apellido, cargo, tienda_id) VALUES (RANDOM_UUID(), 'Alejandro', 'Arroba', 0, '8e9152bb-8e1f-472f-9fe0-0e5349483adf');
INSERT INTO personal (id, nombre, apellido, cargo, tienda_id) VALUES (RANDOM_UUID(), 'Pera', 'DeFruta', 0,'7e9152bb-8e1f-472f-9fe0-0e5349483adf');

