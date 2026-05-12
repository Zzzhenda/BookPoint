-- Datos iniciales para BookPoint Chile.
-- Spring Boot ejecuta este script al arrancar la aplicacion gracias a
-- spring.sql.init.mode=always en application.properties.

-- Productos (catalogo de libros, papeleria y material educativo)
INSERT INTO productos (titulo, autor, editorial, genero, precio, activo) VALUES
('Cien anios de soledad', 'Gabriel Garcia Marquez', 'Sudamericana', 'Novela', 15990, true),
('El laberinto de la soledad', 'Octavio Paz', 'FCE', 'Ensayo', 12500, true),
('Cuaderno universitario 100h', 'N/A', 'Torre', 'Papeleria', 1990, true),
('La casa de los espiritus', 'Isabel Allende', 'Plaza y Janes', 'Novela', 13990, true),
('Set lapices de colores 24u', 'N/A', 'Faber-Castell', 'Material Educativo', 5990, true);

-- Sucursales (las 3 mencionadas en el caso)
INSERT INTO sucursales (nombre, ciudad, direccion, telefono, horario, activa) VALUES
('BookPoint Concepcion', 'Concepcion', 'Av. Ohiggins 1234', '+56 41 2345678', 'Lun-Sab 09:00-20:00', true),
('BookPoint Temuco', 'Temuco', 'Manuel Montt 567', '+56 45 2345678', 'Lun-Sab 10:00-20:00', true),
('BookPoint La Serena', 'La Serena', 'Av. del Mar 890', '+56 51 2345678', 'Lun-Sab 10:00-19:00', true);

-- Usuarios internos (uno por cada rol del PDF)
INSERT INTO usuarios (nombre, email, password, rol, sucursal_id, activo) VALUES
('Admin General', 'admin@bookpoint.cl', 'admin123', 'ADMINISTRADOR', NULL, true),
('Carla Soto', 'carla.soto@bookpoint.cl', 'jefe123', 'JEFE_SUCURSAL', 1, true),
('Pedro Gomez', 'pedro.gomez@bookpoint.cl', 'vendedor123', 'ASISTENTE_VENTAS', 1, true),
('Maria Diaz', 'maria.diaz@bookpoint.cl', 'logistica123', 'LOGISTICA', NULL, true),
('Juan Lopez', 'juan.lopez@bookpoint.cl', 'bodega123', 'BODEGA', NULL, true);

-- Clientes web con sus direcciones
INSERT INTO clientes (nombre, apellido, email, password, telefono, activo) VALUES
('Ana', 'Perez', 'ana.perez@mail.com', 'cliente123', '+56 9 11111111', true),
('Luis', 'Rojas', 'luis.rojas@mail.com', 'cliente123', '+56 9 22222222', true);

INSERT INTO direcciones (calle, ciudad, region, codigo_postal, cliente_id) VALUES
('Av. Colon 100', 'Concepcion', 'Biobio', '4030000', 1),
('Manuel Montt 200', 'Temuco', 'La Araucania', '4780000', 2);

-- Proveedores editoriales
INSERT INTO proveedores (nombre, rut, contacto_nombre, contacto_email, telefono, activo) VALUES
('Editorial Sudamericana', '76123456-7', 'Roberto Munoz', 'rmunoz@sudamericana.cl', '+56 2 22223333', true),
('FCE Chile', '76234567-8', 'Patricia Vega', 'pvega@fce.cl', '+56 2 22224444', true),
('Faber-Castell Chile', '76345678-9', 'Diego Soto', 'dsoto@faber.cl', '+56 2 22225555', true);

-- Stock por sucursal
INSERT INTO stock (producto_id, sucursal_id, cantidad, stock_minimo) VALUES
(1, 1, 25, 5),
(2, 1, 10, 3),
(3, 1, 100, 20),
(1, 2, 15, 5),
(4, 2, 8, 3),
(5, 3, 40, 10);

-- Stock central (bodega)
INSERT INTO stock_central (producto_id, cantidad, stock_minimo, ubicacion) VALUES
(1, 200, 30, 'A-01'),
(2, 150, 20, 'A-02'),
(3, 500, 50, 'B-01'),
(4, 100, 15, 'A-03'),
(5, 300, 40, 'B-02');
