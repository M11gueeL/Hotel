ALTER TABLE clientes ADD COLUMN id_habitacion INTEGER;

ALTER TABLE clientes DROP COLUMN id_habitacion;

-- Cliente de prueba
INSERT INTO clientes (nombre, apellido, cedula, telefono) VALUES ('Juan', 'Perez',31182932,04243132091);

ALTER TABLE clientes MODIFY telefono VARCHAR(30);

ALTER TABLE habitaciones ADD COLUMN nombre VARCHAR(60);

ALTER TABLE reservas CHANGE COLUMN id_habitacion id_habitaciones INTEGER;
ALTER TABLE reservas MODIFY COLUMN fecha_salida DATE;