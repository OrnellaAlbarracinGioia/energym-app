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
-- TABLA: actividades
-- Campos: id, nombre, descripcion, duracion_minutos
-- ============================================
INSERT INTO actividades (id, nombre, descripcion, duracion_minutos) VALUES
                                                                        (1, 'Yoga', 'Clases de yoga para flexibilidad y relajación', 60),
                                                                        (2, 'CrossFit', 'Entrenamiento de alta intensidad funcional', 90),
                                                                        (3, 'Spinning', 'Clases de ciclismo indoor de alta energía', 45),
                                                                        (4, 'Funcional', 'Entrenamiento con movimientos funcionales', 60),
                                                                        (5, 'Pilates', 'Fortalecimiento y flexibilidad profunda', 60),
                                                                        (6, 'Zumba', 'Clases de baile y ritmo latino', 50);

-- ============================================
-- TABLA: clases
-- Campos: id, actividad_id, horario, fecha, entrenador_id, sucursal_id, capacidad_maxima
-- ============================================
INSERT INTO clases (id, actividad_id, horario, fecha, entrenador_id, sucursal_id, capacidad_maxima) VALUES
-- Yoga - Carlos
(1, 1, '08:00:00', '2025-11-04', 1, 1, 20),
(2, 1, '17:30:00', '2025-11-06', 1, 3, 20),

-- CrossFit - María
(3, 2, '18:00:00', '2025-11-05', 2, 1, 15),
(4, 2, '07:30:00', '2025-11-08', 2, 3, 15),

-- Spinning - Juan
(5, 3, '19:00:00', '2025-11-06', 3, 2, 25),
(6, 3, '12:00:00', '2025-11-04', 3, 2, 25),

-- Funcional - Laura
(7, 4, '07:00:00', '2025-11-07', 4, 2, 18),
(8, 4, '09:00:00', '2025-11-05', 4, 1, 18),

-- Pilates - Diego
(9, 5, '10:00:00', '2025-11-08', 5, 3, 15),
(10, 5, '19:30:00', '2025-11-06', 5, 3, 15),

-- Zumba - María
(11, 6, '18:00:00', '2025-11-09', 2, 1, 30),
(12, 6, '20:00:00', '2025-11-07', 2, 2, 30);

-- ============================================
-- TABLA: socios
-- Campos: id, nombre, email, fecha_registro, telefono, activo, clases_personalizadas
-- ============================================
INSERT INTO socios (id, nombre, email, fecha_registro, telefono, activo, clases_personalizadas) VALUES
                                                                                                    (1, 'Ana García', 'ana@example.com', '2025-01-15 10:00:00', '11-1111-2222', true, 0),
                                                                                                    (2, 'Pedro Sánchez', 'pedro@example.com', '2025-02-20 14:30:00', '11-2222-3333', true, 0),
                                                                                                    (3, 'Lucía Fernández', 'lucia@example.com', '2025-03-10 09:15:00', '11-3333-4444', true, 2),
                                                                                                    (4, 'Martín Ruiz', 'martin@example.com', '2025-04-05 16:45:00', '11-4444-5555', true, 5),
                                                                                                    (5, 'Sofía Torres', 'sofia@example.com', '2025-05-12 11:20:00', '11-5555-6666', true, 1),
                                                                                                    (6, 'Roberto Díaz', 'roberto@example.com', '2025-06-01 08:30:00', '11-6666-7777', true, 3),
                                                                                                    (7, 'Valentina Morales', 'valentina@example.com', '2025-07-10 12:15:00', '11-7777-8888', true, 8),
                                                                                                    (8, 'Santiago Romero', 'santiago@example.com', '2025-08-20 17:00:00', '11-8888-9999', true, 0);

-- ============================================
-- TABLA: reservas
-- Campos: id, socio_id, clase_id, fecha, estado, fecha_cancelacion
-- Estados posibles: CONFIRMADA, CANCELADA, COMPLETADA, PENDIENTE
-- ============================================
/*INSERT INTO reservas (id, socio_id, clase_id, fecha, estado, fecha_cancelacion) VALUES
-- Reservas confirmadas (futuras)
(1, 1, 1, '2025-10-28 08:00:00', 'CONFIRMADA', null),
(2, 2, 1, '2025-10-28 09:00:00', 'CONFIRMADA', null),
(3, 3, 1, '2025-10-29 07:00:00', 'CONFIRMADA', null),

-- Más reservas confirmadas
(4, 4, 3, '2025-10-28 15:00:00', 'CONFIRMADA', null),
(5, 5, 3, '2025-10-29 10:00:00', 'CONFIRMADA', null),
(6, 6, 3, '2025-10-29 11:00:00', 'CONFIRMADA', null),

(7, 7, 5, '2025-10-29 18:00:00', 'CONFIRMADA', null),
(8, 8, 5, '2025-10-30 09:00:00', 'CONFIRMADA', null),
(9, 1, 5, '2025-10-30 10:00:00', 'CONFIRMADA', null),

(10, 2, 7, '2025-10-30 06:00:00', 'CONFIRMADA', null),
(11, 3, 7, '2025-10-30 07:00:00', 'CONFIRMADA', null),

(12, 4, 9, '2025-10-30 09:00:00', 'CONFIRMADA', null),
(13, 5, 9, '2025-10-31 08:00:00', 'CONFIRMADA', null),

(14, 6, 11, '2025-10-31 17:00:00', 'CONFIRMADA', null),
(15, 7, 11, '2025-10-31 18:00:00', 'CONFIRMADA', null),

-- Reservas completadas (historial - para sistema de fidelización)
(16, 1, 2, '2025-09-25 08:00:00', 'COMPLETADA', null),
(17, 1, 6, '2025-09-27 17:30:00', 'COMPLETADA', null),
(18, 1, 3, '2025-09-22 12:00:00', 'COMPLETADA', null),
(19, 1, 5, '2025-09-24 19:00:00', 'COMPLETADA', null),

(20, 4, 1, '2025-09-26 18:00:00', 'COMPLETADA', null),
(21, 4, 4, '2025-09-19 07:30:00', 'COMPLETADA', null),
(22, 4, 7, '2025-09-21 07:00:00', 'COMPLETADA', null),
(23, 4, 8, '2025-09-23 09:00:00', 'COMPLETADA', null),
(24, 4, 9, '2025-09-29 10:00:00', 'COMPLETADA', null),

(25, 7, 2, '2025-09-25 19:00:00', 'COMPLETADA', null),
(26, 7, 11, '2025-09-28 18:00:00', 'COMPLETADA', null),
(27, 7, 1, '2025-09-24 08:00:00', 'COMPLETADA', null),
(28, 7, 3, '2025-09-19 18:00:00', 'COMPLETADA', null),
(29, 7, 5, '2025-09-30 10:00:00', 'COMPLETADA', null),
(30, 7, 6, '2025-09-22 17:30:00', 'COMPLETADA', null),
(31, 7, 4, '2025-09-21 12:00:00', 'COMPLETADA', null),
(32, 7, 8, '2025-09-20 07:30:00', 'COMPLETADA', null),

-- Reservas canceladas
(33, 2, 2, '2025-10-10 10:00:00', 'CANCELADA', '2025-10-28 15:00:00'),
(34, 3, 4, '2025-10-12 11:00:00', 'CANCELADA', '2025-10-29 09:00:00'),
(35, 6, 10, '2025-10-14 12:00:00', 'CANCELADA', '2025-10-30 08:00:00'),

-- Reservas pendientes
(36, 2, 12, '2025-10-31 19:00:00', 'PENDIENTE', null),
(37, 8, 2, '2025-10-31 20:00:00', 'PENDIENTE', null);
*/
-- ============================================
-- Resumen de datos cargados:
-- - 3 sucursales
-- - 5 entrenadores
-- - 6 actividades (tipos de clases)
-- - 12 clases (instancias específicas de actividades)
-- - 8 socios (varios con sesiones personalizadas para pruebas)
-- - 37 reservas (15 confirmadas, 16 completadas, 3 canceladas, 2 pendientes)
--
-- Notas importantes:
-- - Socio 7 (Valentina) tiene 8 clases completadas en septiembre
-- - Socio 4 (Martín) tiene 5 clases completadas en septiembre
-- - Se incluyen estados: CONFIRMADA, CANCELADA, COMPLETADA, PENDIENTE
-- - Datos de prueba listos para validar el sistema de reservas
-- ============================================
