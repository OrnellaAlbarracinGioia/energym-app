-- Limpiar datos previos
DELETE FROM reservas;
DELETE FROM clases;
DELETE FROM socios;
DELETE FROM actividades;
DELETE FROM entrenadores;
DELETE FROM sucursales;

-- SUCURSALES
INSERT INTO sucursales (nombre, direccion) VALUES
                                               ('Sucursal Centro', 'Avenida Corrientes 1000'),
                                               ('Sucursal Flores', 'Calle Sarmiento 500');

-- ACTIVIDADES
INSERT INTO actividades (nombre, descripcion, duracion_minutos) VALUES
                                                                    ('Yoga', 'Clase de yoga relajante', 60),
                                                                    ('Pilates', 'Pilates para tonificación', 45),
                                                                    ('Zumba', 'Zumba fitness y diversión', 50),
                                                                    ('Yoga Personalizado', 'Yoga personalizado one-to-one', 60);

-- ENTRENADORES (SIN especialidad, CON contacto)
INSERT INTO entrenadores (nombre, contacto) VALUES
                                                ('María García', 'maria.garcia@gym.com'),
                                                ('Carlos López', 'carlos.lopez@gym.com'),
                                                ('Ana Martínez', 'ana.martinez@gym.com'),
                                                ('Juan Rodríguez', 'juan.rodriguez@gym.com');

-- SOCIOS
INSERT INTO socios (nombre, email, telefono, activo, fecha_registro, clases_personalizadas) VALUES
                                                                                                ('Juan Pérez', 'juan@test.com', '1234567890', true, CURRENT_TIMESTAMP, 0),
                                                                                                ('María López', 'maria@test.com', '0987654321', true, CURRENT_TIMESTAMP, 0),
                                                                                                ('Carlos Díaz', 'carlos@test.com', '5555555555', true, CURRENT_TIMESTAMP, 0);

-- CLASES
INSERT INTO clases (actividad_id, entrenador_id, sucursal_id, fecha, horario, capacidad_maxima, cupos_ocupados, personalizada) VALUES
                                                                                                                      (1, 1, 1, '2025-12-03', '09:00:00', 30,0 , false),
                                                                                                                      (2, 2, 1, '2025-12-03', '10:30:00', 25, 0 ,false),
                                                                                                                      (3, 3, 1, '2025-12-04', '18:00:00', 40,0 ,false),
                                                                                                                      (1, 1, 2, '2025-12-05', '09:00:00', 30, 0 ,false),
                                                                                                                      (2, 2, 2, '2025-12-05', '17:00:00', 25, 0 ,false),
                                                                                                                      (3, 3, 1, '2025-12-06', '19:00:00', 40, 0 ,false),
                                                                                                                      (1, 1, 1, '2025-12-07', '10:00:00', 30, 0 ,false),
                                                                                                                      (4, 4, 1, '2025-12-10', '15:00:00', 5, 0 ,true),
                                                                                                                      (2, 2, 1, '2025-12-10', '09:00:00', 25, 0 ,false),
                                                                                                                      (3, 3, 2, '2025-12-11', '18:30:00', 40, 0 ,false);

-- RESERVAS (Socio 1: 10 clases completadas)

