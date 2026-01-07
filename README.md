Prueba Técnica – API de Transacciones (Reactive)

Este proyecto es una API REST reactiva desarrollada con Spring WebFlux y R2DBC, cuyo objetivo es gestionar transacciones financieras, calculando automáticamente comisiones según reglas de negocio definidas.

La aplicación sigue una arquitectura en capas (Controller → Service → Repository) y maneja validaciones, paginación y control global de excepciones.

🚀 Tecnologías utilizadas

Java 17+

Spring Boot

Spring WebFlux (programación reactiva)

Spring Data R2DBC

Oracle Database (21c XE)

Project Reactor (Mono / Flux)

Lombok

Jakarta Validation

📦 Arquitectura del proyecto
com.sofka.pruebatecnica
├── controller
│   └── TransactionController
├── exception
│   ├── BusinessException
│   ├── NotFoundException
│   └── GlobalExceptionHandler
├── persistence
│   ├── entity
│   │   └── TransactionEntity
│   └── repository
│       └── ITransactionRepository
├── service
│   ├── ITransactionService
│   ├── impl
│   │   └── TransactionImpl
│   └── dto
│       ├── TransactionRequestDto
│       ├── TransactionResponseDto
│       ├── PageResponseDto
│       └── ErrorResponseDto
└── resources
    └── application.yml

📊 Modelo de datos
Tabla transactions
Campo	Tipo	Descripción
id	NUMBER (PK)	Identificador de la transacción
amount	NUMBER	Monto de la transacción
commission	NUMBER	Comisión calculada
creation_date	TIMESTAMP	Fecha de creación
update_date	TIMESTAMP	Fecha de actualización
💼 Reglas de negocio

El monto (amount) debe ser mayor que 0.

La comisión se calcula automáticamente según el monto:

≥ 10.000 → 5%

< 10.000 → 2%

En una operación de creación (POST) se asigna automáticamente la fecha de creación.

En una operación de actualización (PUT) se asigna únicamente la fecha de actualización.

🔌 Endpoints disponibles
➕ Crear transacción

POST /transactions/addNew

Body:
{
  "amount": 15000
}

Respuesta (201):
{
  "id": 1,
  "amount": 15000,
  "commission": 750,
  "creationDate": "2026-01-06T20:30:00"
}

✏️ Actualizar transacción

PUT /transactions/updateData/{id}

Body:
{
  "amount": 5000
}

Respuesta (200):
{
  "id": 1,
  "amount": 5000,
  "commission": 100,
  "creationDate": "2026-01-06T20:30:00"
}
📄 Obtener transacciones paginadas

GET /transactions/getAll?page=1&size=10

Respuesta:
{
  "totalElements": 1,
  "page": 1,
  "size": 10,
  "content": [
    {
      "id": 1,
      "amount": 15000,
      "commission": 750,
      "creationDate": "2026-01-06T20:30:00"
    }
  ]
}

❌ Manejo de errores

La aplicación cuenta con un GlobalExceptionHandler para manejar de forma centralizada las excepciones.

Ejemplo – Error de validación

{
  "message": "Amount must be positive",
  "status": 400,
  "timestamp": "2026-01-06T20:45:00"
}
Tipos de errores manejados

400 BAD REQUEST → Errores de negocio o validación

404 NOT FOUND → Recursos no encontrados

500 INTERNAL SERVER ERROR → Errores inesperados

⚙️ Configuración

La configuración de la aplicación se gestiona mediante el archivo application.yml, el cual referencia valores externos para:

Puerto del servidor

Conexión a la base de datos

Credenciales de acceso

Estos valores no se exponen en el repositorio y deben ser definidos en el entorno donde se despliegue la aplicación.

▶️ Ejecución del proyecto

Asegurar que el entorno de ejecución tenga configurados los valores necesarios para:

Puerto del servidor

Conexión a Oracle Database

Credenciales de acceso

Ejecutar la aplicación con Maven:
mvn spring-boot:run

✅ Características clave

Programación reactiva y no bloqueante

Separación clara de responsabilidades

Validaciones por contexto (Create / Update)

Paginación eficiente

Manejo centralizado de errores

Código limpio y desacoplado

🧑‍💻 Autor

Jefferson Andres Moreno Pedraza
Prueba técnica – Backend (Spring WebFlux)

📌 Este proyecto fue desarrollado como parte de una prueba técnica, aplicando buenas prácticas de diseño, validación y desarrollo de aplicaciones reactivas.
