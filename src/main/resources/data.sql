-- data.sql - Datos iniciales para Energym

-- ============================================
-- TABLA: sucursales
-- Campos: id, nombre, direccion
-- ============================================
INSERT INTO sucursales (id, nombre, direccion) VALUES
                                                   (1, 'Energym Centro', 'Av. Corrientes 1234, CABA'),
                                                   (2, 'Energym Palermo', 'Av. Santa Fe 5678, CABA'),
                                                   (3, 'Energym Belgrano', 'Av. Cabildo 2345, CABA');

-- ============================================
-- TABLA: entrenadores
-- Campos: id, nombre, contacto
-- ============================================
INSERT INTO entrenadores (id, nombre, contacto) VALUES
                                                    (1, 'Carlos Rodríguez', 'carlos@energym.com'),
                                                    (2, 'María López', 'maria@energym.com'),
                                                    (3, 'Juan Pérez', 'juan@energym.com'),
                                                    (4, 'Laura Gómez', 'laura@energym.com'),
                                                    (5, 'Diego Martínez', 'diego@energym.com');

-- ============================================
-- TABLA: socios
-- Campos: id, nombre, email, fecha_registro, telefono, activo, clases_personalizadas
-- ============================================
INSERT INTO socios (id, nombre, email, fecha_registro, telefono, activo, clases_personalizadas) VALUES
                                                                                                    (1, 'Ana García', 'ana@example.com', '2025-01-15 10:00:00', '11-1111-2222', true, 0),
                                                                                                    (2, 'Pedro Sánchez', 'pedro@example.com', '2025-02-20 14:30:00', '11-2222-3333', true, 0),
                                                                                                    (3, 'Lucía Fernández', 'lucia@example.com', '2025-03-10 09:15:00', '11-3333-4444', true, 5),
                                                                                                    (4, 'Martín Ruiz', 'martin@example.com', '2025-04-05 16:45:00', '11-4444-5555', true, 10),
                                                                                                    (5, 'Sofía Torres', 'sofia@example.com', '2025-05-12 11:20:00', '11-5555-6666', true, 3),
                                                                                                    (6, 'Roberto Díaz', 'roberto@example.com', '2025-06-01 08:30:00', '11-6666-7777', true, 8),
                                                                                                    (7, 'Valentina Morales', 'valentina@example.com', '2025-07-10 12:15:00', '11-7777-8888', true, 12),
                                                                                                    (8, 'Santiago Romero', 'santiago@example.com', '2025-08-20 17:00:00', '11-8888-9999', true, 0);

-- ============================================
-- TABLA: clases
-- Campos: id, nombre, capacidad_maxima, dia (0=MONDAY, 1=TUESDAY...), 
--         horario, descripcion, duracion_minutos, entrenador_id, sucursal_id
-- ============================================
INSERT INTO clases (id, nombre, capacidad_maxima, dia, horario, descripcion, duracion_minutos, entrenador_id, sucursal_id) VALUES
                                                                                                                               (1, 'Yoga Matutino', 20, 0, '08:00:00', 'Clase de yoga para comenzar el día con energía', 60, 1, 1),
                                                                                                                               (2, 'CrossFit Intenso', 15, 1, '18:00:00', 'Entrenamiento de alta intensidad', 90, 2, 1),
                                                                                                                               (3, 'Spinning', 25, 2, '19:00:00', 'Clase de ciclismo indoor', 45, 3, 2),
                                                                                                                               (4, 'Funcional Avanzado', 18, 3, '07:00:00', 'Entrenamiento funcional para todo el cuerpo', 60, 4, 2),
                                                                                                                               (5, 'Pilates', 15, 4, '10:00:00', 'Fortalecimiento y flexibilidad', 60, 5, 3),
                                                                                                                               (6, 'Yoga Vespertino', 20, 5, '17:30:00', 'Yoga relajante para terminar el día', 60, 1, 3),
                                                                                                                               (7, 'CrossFit Avanzado', 12, 6, '20:00:00', 'Para atletas experimentados', 90, 2, 1),
                                                                                                                               (8, 'Spinning Express', 30, 0, '12:00:00', 'Clase rápida de spinning al mediodía', 30, 3, 2),
                                                                                                                               (9, 'Funcional Principiantes', 20, 1, '09:00:00', 'Introducción al entrenamiento funcional', 45, 4, 1),
                                                                                                                               (10, 'Pilates Avanzado', 12, 2, '19:30:00', 'Pilates para nivel avanzado', 75, 5, 3),
                                                                                                                               (11, 'Yoga Flow', 18, 3, '18:30:00', 'Secuencias dinámicas de yoga', 60, 1, 2),
                                                                                                                               (12, 'CrossFit Principiantes', 20, 4, '07:30:00', 'Introducción al CrossFit', 60, 2, 3);

-- ============================================
-- TABLA: reservas
-- Campos: id, socio_id, clase_id, fecha_reserva, fecha_clase, estado, fecha_cancelacion
-- Estados posibles: CONFIRMADA, CANCELADA, COMPLETADA
-- ============================================
INSERT INTO reservas (id, socio_id, clase_id, fecha_reserva, fecha_clase, estado, fecha_cancelacion) VALUES
-- Reservas confirmadas (futuras)
(1, 1, 1, '2025-10-20 07:00:00', '2025-11-04 08:00:00', 'CONFIRMADA', null),
(2, 2, 2, '2025-10-21 16:00:00', '2025-11-05 18:00:00', 'CONFIRMADA', null),
(3, 3, 3, '2025-10-22 18:00:00', '2025-11-06 19:00:00', 'CONFIRMADA', null),
(4, 4, 4, '2025-10-23 06:30:00', '2025-11-07 07:00:00', 'CONFIRMADA', null),
(5, 5, 5, '2025-10-24 09:00:00', '2025-11-08 10:00:00', 'CONFIRMADA', null),
(6, 6, 6, '2025-10-25 16:30:00', '2025-11-02 17:30:00', 'CONFIRMADA', null),
(7, 7, 7, '2025-10-26 19:00:00', '2025-11-03 20:00:00', 'CONFIRMADA', null),
(8, 8, 8, '2025-10-27 11:30:00', '2025-11-04 12:00:00', 'CONFIRMADA', null),

-- Reservas completadas (pasadas)
(9, 1, 9, '2025-09-15 08:00:00', '2025-10-15 09:00:00', 'COMPLETADA', null),
(10, 2, 10, '2025-09-16 10:00:00', '2025-10-16 19:30:00', 'COMPLETADA', null),
(11, 3, 11, '2025-09-17 12:00:00', '2025-10-17 18:30:00', 'COMPLETADA', null),
(12, 4, 1, '2025-09-18 07:30:00', '2025-10-21 08:00:00', 'COMPLETADA', null),
(13, 4, 2, '2025-09-19 08:00:00', '2025-10-22 18:00:00', 'COMPLETADA', null),
(14, 4, 3, '2025-09-20 09:00:00', '2025-10-23 19:00:00', 'COMPLETADA', null),
(15, 5, 4, '2025-09-21 10:00:00', '2025-10-24 07:00:00', 'COMPLETADA', null),
(16, 5, 5, '2025-09-22 11:00:00', '2025-10-25 10:00:00', 'COMPLETADA', null),
(17, 6, 6, '2025-09-23 12:00:00', '2025-10-26 17:30:00', 'COMPLETADA', null),
(18, 6, 7, '2025-09-24 13:00:00', '2025-10-27 20:00:00', 'COMPLETADA', null),
(19, 7, 8, '2025-09-25 14:00:00', '2025-10-28 12:00:00', 'COMPLETADA', null),
(20, 7, 9, '2025-09-26 15:00:00', '2025-10-29 09:00:00', 'COMPLETADA', null),

-- Reservas canceladas
(21, 1, 3, '2025-10-10 10:00:00', '2025-10-30 19:00:00', 'CANCELADA', '2025-10-28 15:00:00'),
(22, 2, 5, '2025-10-12 11:00:00', '2025-10-30 10:00:00', 'CANCELADA', '2025-10-29 09:00:00'),
(23, 3, 7, '2025-10-14 12:00:00', '2025-10-31 20:00:00', 'CANCELADA', '2025-10-30 08:00:00');

-- ============================================
-- Resumen de datos cargados:
-- - 3 sucursales
-- - 5 entrenadores
-- - 8 socios (algunos con clases_personalizadas para probar el sistema de fidelización)
-- - 12 clases (distribuidas en diferentes días y horarios)
-- - 23 reservas (8 confirmadas, 12 completadas, 3 canceladas)
-- ============================================
