# Mock API - Customers / Products / Orders (Spring Boot + Java 21)

API mock construida con **Java 21** y **Spring Boot**, pensada para practicar consumo de APIs REST y cubrir casos comunes de integración (paginación, filtros, catálogos, relaciones y estados).  
Por ahora, el foco está en **respuestas HTTP 2xx**, principalmente **200 OK** para consultas.

---

## ¿Qué hace esta API hasta este punto?

La API expone un modelo simple tipo e-commerce:

- **Customers**: clientes con `status` (ACTIVE / SUSPENDED)
- **Products**: productos con `active`, `sku`, `price`
- **Orders**: órdenes con `status` (CREATED / PAID / CANCELLED), asociadas a un customer
- **OrderItems**: ítems por orden con cantidad y precios

### Funcionalidades implementadas (v0)
✅ Persistencia local en memoria con **H2**  
✅ Seeding automático de datos al iniciar (customers, products, orders + items)  
✅ Endpoints REST para listar y consultar por id (200 OK)  
✅ Paginación y ordenamiento usando `Pageable`  
✅ Filtros y búsquedas simples por query params  
✅ Actuator (health/info/metrics)  
✅ (Opcional) Swagger/OpenAPI si agregaste `springdoc-openapi`

> Próximos pasos típicos: `201 Created` para crear órdenes, `204 No Content` para cancelación/borrado, y manejo formal de errores (`404`, `400`, `409`, etc).

---

## Tecnologías

- Java 21
- Spring Boot
- Spring Web (MVC)
- Spring Data JPA
- H2 Database (in-memory)
- Actuator
- (Opcional) Swagger/OpenAPI (springdoc)

---

## Requisitos

- **Java 21** instalado y configurado en `JAVA_HOME`
- **Gradle** (o usar el wrapper `./gradlew` / `gradlew.bat`)
- Git (si vas a versionar y crear tags)

---

## Cómo correr el aplicativo

### 1) Clonar el repo
```bash
git clone <tu_repo_url>
cd <tu_repo_folder>
```

### 2) Ejecutar con Gradle Wrapper

**Windows (PowerShell / CMD):**
```bash
./gradlew bootRun
```

**Linux / macOS:**
```bash
./gradlew bootRun
```

La app inicia en:
- `http://localhost:8080`

---

## Base de datos H2

La aplicación usa H2 en memoria (se crea al iniciar y se destruye al apagar).

- JDBC: `jdbc:h2:mem:mockdb`
- Consola H2: `http://localhost:8080/h2`
  - Driver: `org.h2.Driver`
  - JDBC URL: `jdbc:h2:mem:mockdb`
  - User: `sa`
  - Password: (vacío)

---

## Datos iniciales (Seeder)

Al arrancar, se crean registros de ejemplo:

- Customers: Ana, Luis, Sofía (con estados)
- Products: SKU-1001.. etc (algunos activos/inactivos)
- Orders: ORD-0001.. etc con items

Esto permite probar la API sin crear datos manualmente.

---

## Endpoints disponibles

### Customers

#### Listar customers (paginado)
`GET /api/customers`

Query params:
- `page` (default 0)
- `size` (default 20)
- `sort` (ej: `id,asc`)
- `status` (opcional: `ACTIVE` | `SUSPENDED`)
- `q` (opcional: búsqueda por nombre o email)

Ejemplos:
```bash
curl "http://localhost:8080/api/customers?page=0&size=10&sort=id,asc"
curl "http://localhost:8080/api/customers?status=ACTIVE"
curl "http://localhost:8080/api/customers?q=ana"
```

#### Obtener customer por id
`GET /api/customers/{id}`

```bash
curl "http://localhost:8080/api/customers/1"
```

---

### Products

#### Listar products (paginado)
`GET /api/products`

Query params:
- `page`, `size`, `sort`
- `active` (opcional: true/false)
- `q` (opcional: búsqueda por name o sku)
- `minPrice` y `maxPrice` (opcional: rango de precios; ideal enviarlos juntos)

Ejemplos:
```bash
curl "http://localhost:8080/api/products?page=0&size=10"
curl "http://localhost:8080/api/products?active=true"
curl "http://localhost:8080/api/products?q=usb"
curl "http://localhost:8080/api/products?minPrice=100000&maxPrice=500000"
```

#### Obtener product por id
`GET /api/products/{id}`

```bash
curl "http://localhost:8080/api/products/1"
```

---

### Orders

#### Listar orders (paginado)
`GET /api/orders`

Query params:
- `page`, `size`, `sort`
- `customerId` (opcional)
- `status` (opcional: `CREATED` | `PAID` | `CANCELLED`)
- `from` y `to` (opcional; formato ISO-8601 para filtrar por createdAt)

Ejemplos:
```bash
curl "http://localhost:8080/api/orders?page=0&size=10&sort=id,desc"
curl "http://localhost:8080/api/orders?customerId=1"
curl "http://localhost:8080/api/orders?status=PAID"
```

#### Obtener order por id
`GET /api/orders/{id}`

```bash
curl "http://localhost:8080/api/orders/1"
```

---

## Actuator

Endpoints expuestos:
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`

Ejemplo:
```bash
curl "http://localhost:8080/actuator/health"
```

---

## Swagger / OpenAPI (si está habilitado)

- Swagger UI: `GET /swagger-ui/index.html`
- OpenAPI JSON: `GET /v3/api-docs`

---

## Postman

Puedes importar la colección Postman incluida (si la agregaste al repo), o usar la colección que generamos en la conversación.

Recomendación: setea la variable:
- `baseUrl = http://localhost:8080`

---

## Troubleshooting

### Error en seeding: NullPointerException en BigDecimal.add (Order.recalcTotals)
Si el total se calcula a partir de `lineTotal` antes de persistir, `lineTotal` puede ser null.  
Solución recomendada: recalcular el total usando `unitPrice * quantity` en `Order.recalcTotals()`.

---

## Versionado (Git tags)

Para marcar una versión:

```bash
git add .
git commit -m "chore: initial mock API with GET endpoints, H2 seeding, actuator"
git tag -a v0.1.0 -m "v0.1.0: initial mock API (customers/products/orders), H2 seeding, paging & filters"
git push origin v0.1.0
```

---

## Roadmap (próximo)
- `POST /api/orders` → `201 Created` + Location
- Manejo consistente de errores:
  - `404 Not Found` (entidad no existe)
  - `400 Bad Request` (validaciones)
  - `409 Conflict` (unicidad sku/email/orderNumber)
- Respuestas con Problem Details (RFC 9457) o formato estándar
