# Energym Sistema de Reservas

## Introducción
Este sistema de gestión de reservas está orientado al cliente Energym para poder optimizar la disponibilidad y organización del Gimnasio.

## Funcionalidades Implementadas
-  Registro y gestión de clases
-  Sistema de reservas con validación de cupos
-  Prevención de duplicados
-  Cancelación de reservas
-  Beneficio: 1 clase personalizada gratis cada 10 clases completadas
-  Historial de asistencia por socio

## Base de datos (H2)

### Tablas principales:
- **socios**: Información de usuarios
- **clases**: Clases disponibles
- **reservas**: Reservas de socios a clases
- **actividades**: Tipos de actividades
- **entrenadores**: Entrenadores que brindan clases
- **sucursales**: Ubicaciones

## Tecnologías
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Lombok
- Swagger/OpenAPI

## Explicación GENERAL
Este sistema cuenta con varios endpoints fundamentales que contribuyen a un esquema de solución para la problemática planteada.
De esta manera, el enfoque plantea la existencia de un Usuario Administrador que generaria primeramente
las sucursales adheridas al gimnasio, las mismas utilizarian los Endpoints de la API Sucursal.
Además, se debe tener en cuenta que se contempla la generación de Entrenadores los cuales se deberían registrar en principio para poder dictar y registrar clases utilizando los
Endpoints de la API Entrenador.

Por otro lado, se cuenta con las Actividades, el objetivo de ellas son para mantener una coherencia entre las Clases, por lo que se podrían considerar
como una lista que completaria también el Usuario Administrador para poder determinar que categorías son admitidas en el Gimnasio (Endpoints de Actividad).
Cada clase es considerada como una instancia en el tiempo de cada Actividad, es llevada a cabo por un Entrenador (previamente registrado) en una Sucursal del Gimnasio (previamente cargada en sistema), esta funcionalidad está contemplada en la API de Clase (Endpoints).

En base a esto, los Socios (previamente registrados desde la API de Socio), pueden solicitar o gestionar el estado de las reservas de cada Clase.
Asimismo, las Reservas se almacenan y pueden cambiar a medida de las necesidades del Socio.

Finalmente, este sistema genera un flujo que busca controlar la disponibilidad y organización del Gimnasio Energym.




