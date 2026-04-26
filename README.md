<div align="center">

# De Casa · Bruselas

**Plataforma de pedidos online para un catering español artesano —
API REST completa con autenticación JWT y frontend Angular, construida desde cero.**

[![Java](https://img.shields.io/badge/Java_21-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot_4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Angular](https://img.shields.io/badge/Angular_17+-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

</div>

---

## ¿Qué es esto?

De Casa es un catering español artesano en Bruselas — la tortilla, las croquetas, el pan de las mini burgers, todo hecho en casa desde cero.

Este repositorio es la plataforma digital que gestiona los pedidos: catálogo público, formulario de pedido con reglas de negocio reales, seguimiento por número de pedido y panel de administración protegido con JWT.

| Qué | Cómo |
|-----|------|
| **Backend** | Spring Boot 4 · Spring Security 7 · JWT (JJWT 0.12) |
| **Frontend** | Angular 17+ (standalone components, signals) |
| **Persistencia** | Spring Data JPA · Hibernate · PostgreSQL |
| **Migraciones** | Flyway — schema versionado |
| **Arquitectura** | REST API desacoplada · CORS configurado · JWT stateless |

---

## Índice

- [Funcionalidades](#funcionalidades)
- [Decisiones técnicas](#decisiones-técnicas)
- [Arquitectura](#arquitectura)
- [API — Endpoints](#api--endpoints)
- [Modelo de datos](#modelo-de-datos)
- [Instalación local](#instalación-local)
- [Contacto](#contacto)

---

## Funcionalidades

### Catálogo público
- Menú completo organizado por categorías (tapas, cocina, horno, postres…)
- Packs para eventos con componentes fijos o "a elección del chef"
- Especiales de la semana (tapa y mini), gestionados desde el panel admin
- Productos exclusivos (`exclusive = true`) que no aparecen en el menú público — solo por encargo

### Pedidos
- Formulario con validación de reglas de negocio: mínimo 48h de antelación, mínimo €40 para entrega / €20 para recogida, mínimo de unidades por producto
- Número de pedido único generado en el servidor
- Seguimiento público por número de pedido
- Snapshot de precio en cada línea de pedido — un cambio de precio no afecta pedidos existentes

### Panel admin (protegido)
- Login con email/contraseña → JWT con expiración de 24h
- Gestión completa del menú (crear, editar, desactivar productos)
- Gestión de packs y especiales de la semana
- Vista de pedidos con cambio de estado (PENDING → CONFIRMED → IN_PREPARATION → READY → DELIVERED)

---

## Decisiones técnicas

**JWT stateless con interceptor Angular** — el servidor no guarda sesión. Cada petición al área admin lleva el token en el header `Authorization: Bearer`. El interceptor de Angular lo adjunta automáticamente a cada petición, sin tener que añadirlo a mano.

**Snapshot de precio en `order_item`** — al confirmar un pedido se copian nombre y precio del producto en ese momento. Permite que el negocio actualice precios sin que afecte al historial de pedidos.

**`exclusive = true` para productos de encargo** — algunos productos (steak tartar, opciones premium) no se muestran en el menú público. Se gestionan desde el admin y se ofrecen directamente al cliente por WhatsApp.

**Circular dependency en Spring Security resuelta** — `SecurityConfig` inyecta `JwtAuthenticationFilter`, que a su vez necesita `UserDetailsService`. Al definir `UserDetailsService` en la misma clase se creaba un ciclo que Spring Boot 4 rechaza por defecto. Solución: extraer `UserDetailsService` y `PasswordEncoder` a una clase `UserDetailsConfig` independiente, rompiendo el ciclo sin hacks (`allow-circular-references`).

**Standalone components (Angular 17+)** — la app no usa `NgModule`. Cada componente declara sus propias dependencias con `imports: [...]` y la configuración global va en `app.config.ts` con `provideRouter()` y `provideHttpClient()`. Más explícito y más fácil de razonar sobre dependencias.

**Lazy loading por ruta** — cada página se carga solo cuando el usuario navega a ella. La ruta `/admin` aplica el guard de autenticación antes de descargar el bundle.

---

## Arquitectura

```
Navegador
    │
    │  HTTP + JSON
    ▼
Angular SPA (:4200)
    │  ← JwtInterceptor añade Bearer token automáticamente
    │
    │  REST API
    ▼
Spring Boot (:8080)
    │  ← JwtAuthenticationFilter valida cada petición protegida
    │
    │  JPA / Hibernate
    ▼
PostgreSQL (:5432)
```

```
src/main/java/org/proyecto/decasa/
├── domain/
│   ├── entity/        Entidades JPA (Product, Order, Pack, WeeklySpecial…)
│   └── enums/         CategoryType, OrderStatus, DeliveryType…
├── infrastructure/
│   └── repository/    Repositorios Spring Data JPA
├── application/
│   └── service/       Lógica de negocio (OrderService, ProductService…)
├── api/
│   ├── controller/    Controladores REST públicos y admin
│   └── dto/           DTOs de request y response
├── security/          JwtTokenProvider, JwtAuthenticationFilter, SecurityConfig
└── exception/         GlobalExceptionHandler, excepciones de dominio

frontend/src/app/
├── core/
│   ├── services/      AuthService, MenuService, OrderService
│   ├── interceptors/  JwtInterceptor
│   └── guards/        AuthGuard
├── features/
│   ├── menu/          Catálogo público
│   ├── order/         Formulario de pedido y tracking
│   └── admin/         Login y panel de gestión
└── shared/
    └── models/        Interfaces TypeScript (espejo de los DTOs Java)
```

---

## API — Endpoints

### Público (sin autenticación)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/menu` | Menú completo: productos por categoría, packs y especiales |
| `GET` | `/api/products` | Todos los productos disponibles |
| `GET` | `/api/products/category/{type}` | Productos por categoría |
| `GET` | `/api/packs` | Packs para eventos |
| `GET` | `/api/weekly-specials/current` | Tapa y mini de la semana activos |
| `POST` | `/api/orders` | Crear un pedido |
| `GET` | `/api/orders/{orderNumber}` | Seguimiento de pedido |
| `POST` | `/api/auth/login` | Login admin → devuelve JWT |

### Admin — requiere `Authorization: Bearer <token>`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/admin/orders` | Todos los pedidos |
| `PATCH` | `/api/admin/orders/{id}/status` | Cambiar estado del pedido |
| `POST / PUT / DELETE` | `/api/admin/products/**` | Gestión del catálogo |
| `POST` | `/api/admin/weekly-specials` | Establecer especial de la semana |
| `POST / PUT` | `/api/admin/packs/**` | Gestión de packs |

---

## Modelo de datos

```
category ──< product >── weekly_special
               │
               └──< pack_component >── pack
                      (product_id nullable = "a elección del chef")

orders ──< order_item ──► product
  │                  └──► pack
  └── payment  (integración Mollie — pendiente)
```

**Reglas de negocio en el schema:**
- `orders.delivery_type = 'PICKUP' OR delivery_address IS NOT NULL` — constraint SQL
- `order_item`: exactamente un `product_id` o un `pack_id` por línea, nunca ambos
- `weekly_special`: unique sobre `(type, week_of)` — solo un especial por tipo por semana

---

## Instalación local

**Requisitos:** Java 21+, PostgreSQL, Node.js 20+, Angular CLI

```bash
# 1. Clonar el repositorio
git clone https://github.com/Albertocaen/DeCasa.git
cd DeCasa

# 2. Crear la base de datos
psql -U postgres -c "CREATE DATABASE decasa;"

# 3. Arrancar el backend (Hibernate crea el schema automáticamente en dev)
mvnw.cmd spring-boot:run          # Windows
./mvnw spring-boot:run            # Mac / Linux
# API disponible en http://localhost:8080

# 4. En otra terminal — arrancar el frontend
cd frontend
npm install
ng serve
# App disponible en http://localhost:4200
```

**Credenciales de desarrollo** (definidas en `application.properties`):
```
Admin:     admin@decasa.be / admin123
PostgreSQL: postgres / postgres
```

---

## Contacto

- **Autor:** Alberto Caen
- **GitHub:** [github.com/Albertocaen](https://github.com/Albertocaen)
- **LinkedIn:** [linkedin.com/in/albertocaen77](https://www.linkedin.com/in/albertocaen77)
- **Email:** alberto.caen.1@gmail.com

---

## Licencia

MIT
