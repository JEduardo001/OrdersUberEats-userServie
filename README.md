User Service
Microservicio dedicado a la gestión de perfiles de usuario y datos personales. Actúa como consumidor del flujo de registro iniciado por el servicio de autenticación para garantizar la creación de la entidad de usuario correspondiente.

Stack Tecnológico
Java: 21

Framework: Spring Boot 4.0.1

Base de Datos: PostgreSQL

Mensajería: Apache Kafka

Service Discovery: Netflix Eureka

API Endpoints
Base Path: /api/v1/user

GET /: Recupera una lista paginada de todos los usuarios registrados.

GET /{idUser}: Obtiene la información detallada de un usuario específico mediante su UUID.

PUT /: Actualiza los datos de perfil de un usuario existente.

Integración Asíncrona (Kafka)
El servicio mantiene la integridad de los datos de usuario respondiendo a las solicitudes de creación de otros servicios del ecosistema.

Eventos Consumidos
creating.user: Recibe la solicitud de creación de un nuevo perfil. El servicio procesa la persistencia y verifica si el usuario ya existe.

Eventos Producidos
creating.user.response: Notifica el resultado de la operación (éxito, error o ya existente) al servicio de origen para completar el flujo de registro.

failed.send.event.dlq: Canal para gestionar eventos que no pudieron ser entregados al broker.

Patrones y Trazabilidad
Transactional Outbox: Utiliza OutboxEventService para asegurar que la respuesta del evento se registre de forma consistente con la creación del usuario en la base de datos.

Idempotencia: Gestión de eventos mediante ProcessedEventService para evitar duplicidad de registros.

Contexto de Diagnóstico: Uso de MappedDiagnosticService para propagar y persistir el correlationId recibido en las cabeceras de Kafka.

Configuración de Infraestructura
Puerto: 5011

Base de Datos: UserServiceOrderUberEatsDB

Discovery: Registro automático en Eureka para comunicación inter-servicios.
