/* Populate tables */
INSERT INTO clientes (nombre, apellido, email, fecha, created_at, updated_at, foto) VALUES ('Gustavo', 'marquez', 'full@gmail.com', '2022-08-28', '2022-08-28', '2022-08-28',  '');
INSERT INTO clientes (nombre, apellido, email, fecha, created_at, updated_at, foto) VALUES ('John', 'Doe', 'john.doe@gmail.com', '2017-08-28', '2017-08-28', '2022-08-28', '');


/* Populate tabla productos */
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Panasonic Pantalla LCD', 259990, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Sony Camara digital DSC-W320B', 123490, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Apple iPod shuffle', 1499990, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Sony Notebook Z110', 37990, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Hewlett Packard Multifuncional F2280', 69990, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Bianchi Bicicleta Aro 26', 69990, NOW(), NOW());
INSERT INTO productos (nombre, precio, created_at, updated_at) VALUES('Mica Comoda 5 Cajones', 299990, NOW(), NOW());


/*Una factura con items*/
INSERT INTO facturas (descripcion, observacion, cliente_id, created_at, updated_at) VALUES('Factura equipos de oficina', null, 1, NOW(), NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id, created_at, updated_at) VALUES(1, 1, 1, NOW(), NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id, created_at, updated_at) VALUES(2, 1, 4, NOW(), NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id, created_at, updated_at) VALUES(1, 1, 5, NOW(), NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id, created_at, updated_at) VALUES(1, 1, 7, NOW(), NOW());



/* Creamos algunos usuarios con sus roles */
INSERT INTO users (username, password, enabled) VALUES ('user','$2a$10$HeHskXahpRQcNCsnVUNB6eBu18kjbdMVrSF5j5Hg7T7n8y6m09ILO',1);
INSERT INTO users (username, password, enabled) VALUES ('admin','$2a$10$HeHskXahpRQcNCsnVUNB6eBu18kjbdMVrSF5j5Hg7T7n8y6m09ILO',1);

INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');