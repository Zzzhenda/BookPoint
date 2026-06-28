# BookPoint Chile - Sistema de Microservicios

Proyecto semestral de la asignatura Desarrollo FullStack 1 (DSY1103, Duoc UC).

BookPoint Chile es una librería y papelería con sucursales en Concepción, Temuco y La Serena.
Su antiguo sistema monolítico presentaba lentitud, stock desactualizado y demoras en los despachos.
Este proyecto moderniza ese sistema dividiéndolo en microservicios independientes, donde cada uno
tiene una responsabilidad única, su propia base de datos y su propio puerto, comunicándose entre sí
por HTTP a través de un API Gateway.

## Integrantes

- Victor Gutierrez
- Benjamin Figueroa

## Descripción general

En lugar de una sola aplicación grande, el sistema está formado por ocho microservicios de negocio y
un gateway. Cada microservicio se construye con el patrón CSR (Controller, Service, Repository),
persiste sus datos con JPA e Hibernate sobre MySQL, valida la información de entrada con Bean Validation
y maneja los errores de forma centralizada. Cuando un microservicio necesita información que pertenece a
otro, no accede a su base de datos: se la pide por HTTP usando WebClient.

## Tecnologías

- Spring Boot 4.0.5 y Java 17
- Spring Data JPA e Hibernate
- MySQL (a través de XAMPP)
- Bean Validation (JSR 380)
- WebClient para la comunicación entre microservicios
- Spring Cloud Gateway para el enrutamiento
- springdoc-openapi (Swagger) para la documentación de las APIs
- JUnit 5, Mockito y DataFaker para las pruebas unitarias
- Lombok
- Maven

## Requisitos previos

- JDK 17 instalado.
- XAMPP con MySQL en ejecución (usuario root sin contraseña, puerto 3306, que es la configuración por defecto).
- VSCODE.

No es necesario crear las bases de datos manualmente. Cada microservicio crea la suya la primera vez que
arranca, gracias al parámetro createDatabaseIfNotExist en la URL de conexión, e Hibernate crea las tablas
a partir de las entidades (ddl-auto=update).

## Estructura del repositorio

Cada microservicio es un proyecto Maven independiente con la siguiente estructura interna:

```
controller/   recibe las peticiones REST
service/      lógica de negocio
repository/   acceso a datos (JpaRepository)
model/        entidades JPA
dto/          objetos de entrada y salida
exception/    manejo de errores
config/       configuración (Swagger, WebClient)
```

Las carpetas en la raíz del repositorio son:

```
ms-clientes/     ms-productos/    ms-inventario/   ms-carrito/
ms-pedidos/      ms-ventas/       ms-despachos/    ms-sucursales/
gateway/
```

## Microservicios

| Microservicio | Puerto | Base de datos | Responsabilidad | Servicios que consume |
|---------------|--------|---------------|-----------------|-----------------------|
| ms-clientes | 8081 | clientes_db | Registro, perfil y direcciones del cliente | Ninguno |
| ms-productos | 8082 | productos_db | Catálogo de libros y útiles | Ninguno |
| ms-inventario | 8083 | inventario_db | Stock por sucursal y alertas de reposición | Ninguno |
| ms-carrito | 8084 | carrito_db | Carrito de compra | ms-productos |
| ms-pedidos | 8085 | pedidos_db | Genera pedidos a partir del carrito | ms-clientes, ms-carrito |
| ms-ventas | 8086 | ventas_db | Registra la boleta de un pedido | ms-pedidos |
| ms-despachos | 8087 | despachos_db | Controla el estado de envío de un pedido | ms-pedidos |
| ms-sucursales | 8088 | sucursales_db | Datos de las sucursales | Ninguno |
| gateway | 8080 | No usa base de datos | Punto de entrada único que enruta a los ocho microservicios | Enruta a todos |

## Comunicación entre microservicios

La comunicación se realiza por HTTP con WebClient de forma síncrona. Cada microservicio que necesita datos
de otro encapsula la llamada en una clase de la carpeta client, que devuelve un Optional y maneja el caso
de que el recurso no exista o el servicio no esté disponible.

- ms-carrito consulta a ms-productos para validar el producto y obtener su título y precio.
- ms-pedidos consulta a ms-clientes para validar el cliente y a ms-carrito para obtener los productos.
- ms-ventas consulta a ms-pedidos para validar el pedido y tomar su total.
- ms-despachos consulta a ms-pedidos para validar el pedido.

## Flujo de negocio principal

1. Se registra un cliente en ms-clientes.
2. Se crea un producto en ms-productos.
3. Se crea un carrito para el cliente en ms-carrito y se le agregan productos.
4. Se genera un pedido a partir del carrito en ms-pedidos.
5. Se registra la venta o boleta del pedido en ms-ventas.
6. Se crea el despacho del pedido en ms-despachos y se actualiza su estado hasta la entrega.

## Cómo ejecutar el proyecto

1. Iniciar MySQL desde el panel de XAMPP.
2. Abrir cada microservicio en el IDE y ejecutar su clase principal, en el siguiente orden para que cada
   servicio encuentre disponibles a los que necesita:
   1. ms-sucursales, ms-clientes, ms-productos y ms-inventario (no dependen de otros).
   2. ms-carrito (necesita ms-productos).
   3. ms-pedidos (necesita ms-clientes y ms-carrito).
   4. ms-ventas y ms-despachos (necesitan ms-pedidos).
   5. gateway (al final).
3. También se puede ejecutar cada uno desde la terminal con `mvn spring-boot:run` dentro de su carpeta.

Una vez levantados, las peticiones pueden hacerse directamente al puerto de cada microservicio o a través
del gateway en el puerto 8080. Por ejemplo, `http://localhost:8080/api/productos` es reenviado por el
gateway a ms-productos.

## Documentación de las APIs

Cada microservicio expone su documentación interactiva de Swagger en:

```
http://localhost:<puerto>/swagger-ui.html
```

Por ejemplo, `http://localhost:8082/swagger-ui.html` para el catálogo de productos.

## Pruebas

Cada microservicio incluye dos clases de pruebas unitarias: una para la capa de servicio, que utiliza
Mockito para simular el repositorio, y otra para la capa de controlador, con la anotación WebMvcTest.
Los datos de prueba se generan con DataFaker. Para ejecutarlas, dentro de la carpeta de cada microservicio:

```
mvn test
```

Las pruebas unitarias no requieren que MySQL esté en ejecución.

## Notas técnicas

- El gateway utiliza Spring Cloud 2025.1.2 con el starter spring-cloud-starter-gateway-server-webflux, que
  es la versión compatible con Spring Boot 4. Las rutas se configuran en application.properties con el
  prefijo spring.cloud.gateway.server.webflux.routes.
- La configuración de conexión a la base de datos apunta a la instalación por defecto de XAMPP (usuario
  root sin contraseña). Si su MySQL tiene contraseña, debe actualizarse en el application.properties de
  cada microservicio.
- Los datos se almacenan en el servidor MySQL de la máquina donde se ejecuta el proyecto, no dentro del
  repositorio. Al mover el proyecto a otra máquina, las bases de datos se vuelven a crear automáticamente
  al arrancar.
