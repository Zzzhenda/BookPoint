# BookPoint Chile - API REST

API REST para BookPoint Chile.

## Integrantes

- VICTOR GUTIERREZ 
- BENJAMIN


## Tecnologias

- Java 21
- Spring Boot 3.5.14
- Spring Web (REST)
- Spring WebFlux (WebClient para comunicacion entre microservicios)
- Spring Data JPA + Hibernate
- Spring Boot Validation (Bean Validation JSR 380)
- Base de datos H2 (en memoria)
- Lombok
- SLF4J (logs)
- Maven

## Estructura del proyecto

```
src/main/java/com/bookpoint/books/
├── BooksApplication.java         Punto de entrada Spring Boot
├── config/                       Configuracion (WebClient)
├── exception/                    Excepciones y ControllerAdvice global
├── productos/                    Modulo: catalogo
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── dto/
├── sucursales/                   Modulo: sucursales fisicas
├── usuarios/                     Modulo: usuarios internos
├── clientes/                     Modulo: clientes web (con direcciones)
├── proveedores/                  Modulo: proveedores editoriales
├── inventario/                   Modulo: stock por sucursal
├── bodega/                       Modulo: stock central y recepciones
├── ventas/                       Modulo: ventas en caja
├── pedidos/                      Modulo: pedidos online (con PedidoClient)
└── despachos/                    Modulo: envios a domicilio
```

Cada modulo sigue el patron **CSR** (Controller-Service-Repository) con DTOs
separados de las entidades JPA.

## Comunicacion entre microservicios

El modulo `despachos` no acopla directamente al de `pedidos`: consume su API
REST a traves de un `WebClient` configurado en
[`config/WebClientConfig.java`](src/main/java/com/bookpoint/books/config/WebClientConfig.java)
con timeouts de conexion y lectura. El cliente vive en
[`pedidos/client/PedidoClient.java`](src/main/java/com/bookpoint/books/pedidos/client/PedidoClient.java)
y traduce los errores HTTP (404, 5xx, timeout) a las excepciones propias del
dominio (`RecursoNoEncontradoException`, `ReglaNegocioException`).

La URL base se configura en `application.properties`
(`bookpoint.api.base-url`) para que en despliegues separados solo haya que
cambiar esa propiedad.

## Como ejecutar

Desde la carpeta raiz del proyecto:

```bash
./mvnw spring-boot:run
```

(En Windows: `mvnw.cmd spring-boot:run`)

La API queda escuchando en `http://localhost:8080`.

### Consola H2

`http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:bookpointdb`
- Usuario: `sa`
- Password: vacio

## Endpoints REST

Todos los endpoints siguen las convenciones REST y devuelven JSON.

### Productos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET    | `/api/productos`                       | Listar todos |
| GET    | `/api/productos/{id}`                  | Buscar por id |
| GET    | `/api/productos/autor/{autor}`         | Filtrar por autor |
| GET    | `/api/productos/editorial/{editorial}` | Filtrar por editorial |
| GET    | `/api/productos/genero/{genero}`       | Filtrar por genero |
| POST   | `/api/productos`                       | Crear |
| PUT    | `/api/productos/{id}`                  | Actualizar |
| DELETE | `/api/productos/{id}`                  | Eliminar |

### Sucursales

`/api/sucursales` - GET, GET/{id}, POST, PUT/{id}, DELETE/{id}

### Usuarios

`/api/usuarios` - GET, GET/{id}, POST, PUT/{id}, DELETE/{id}

### Clientes

`/api/clientes` - GET, GET/{id}, POST, PUT/{id}, DELETE/{id}
(El payload incluye direcciones, se manejan en cascada)

### Proveedores

`/api/proveedores` - GET, GET/{id}, POST, PUT/{id}, DELETE/{id}

### Inventario (stock por sucursal)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET    | `/api/stock`                          | Listar stock |
| GET    | `/api/stock/{id}`                     | Buscar por id |
| GET    | `/api/stock/sucursal/{sucursalId}`    | Stock de una sucursal |
| POST   | `/api/stock`                          | Crear |
| PUT    | `/api/stock/{id}`                     | Actualizar |
| DELETE | `/api/stock/{id}`                     | Eliminar |

### Bodega

`/api/stock-central` - CRUD del stock central
`/api/recepciones` - GET, GET/{id}, POST, DELETE/{id} (al crear se incrementa el stock central)

### Ventas

`/api/ventas` - GET, GET/{id}, POST, DELETE/{id} (al crear se descuenta stock)

### Pedidos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET    | `/api/pedidos`                       | Listar todos |
| GET    | `/api/pedidos/{id}`                  | Buscar por id |
| GET    | `/api/pedidos/cliente/{clienteId}`   | Pedidos de un cliente |
| POST   | `/api/pedidos`                       | Crear (queda PENDIENTE) |
| POST   | `/api/pedidos/{id}/confirmar`        | Confirmar (descuenta stock) |
| POST   | `/api/pedidos/{id}/cancelar`         | Cancelar |
| PUT    | `/api/pedidos/{id}/estado?estado=X`  | Cambiar estado |
| DELETE | `/api/pedidos/{id}`                  | Eliminar |

### Despachos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET    | `/api/despachos`              | Listar |
| GET    | `/api/despachos/{id}`         | Buscar por id |
| POST   | `/api/despachos`              | Crear (valida pedido via WebClient) |
| POST   | `/api/despachos/{id}/en-ruta` | Pasar a EN_RUTA |
| POST   | `/api/despachos/{id}/entregar`| Marcar ENTREGADO (sincroniza pedido via WebClient) |
| POST   | `/api/despachos/{id}/cancelar`| Cancelar |
| DELETE | `/api/despachos/{id}`         | Eliminar |

## Datos iniciales

El archivo `src/main/resources/data.sql` carga datos de ejemplo al arrancar:

- 5 productos del catalogo
- 3 sucursales (Concepcion, Temuco, La Serena)
- 5 usuarios internos (uno por rol)
- 2 clientes con sus direcciones
- 3 proveedores
- Stock inicial por sucursal y stock central

## Flujo de prueba sugerido

1. `GET /api/productos` - ver el catalogo.
2. `POST /api/ventas` - registrar una venta (descuenta stock automaticamente).
3. `POST /api/pedidos` - crear pedido online (PENDIENTE).
4. `POST /api/pedidos/{id}/confirmar` - confirmar (descuenta stock).
5. `POST /api/despachos` - crear despacho para ese pedido (valida via WebClient).
6. `POST /api/despachos/{id}/en-ruta` - cambiar estado.
7. `POST /api/despachos/{id}/entregar` - entregar (sincroniza el pedido a ENTREGADO via WebClient).
