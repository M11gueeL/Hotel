ALTER TABLE clientes ADD COLUMN id_habitacion INTEGER;

ALTER TABLE clientes DROP COLUMN id_habitacion;

-- Cliente de prueba
INSERT INTO clientes (nombre, apellido, cedula, telefono) VALUES ('Juan', 'Perez',31182932,04243132091);

ALTER TABLE clientes MODIFY telefono VARCHAR(30);

ALTER TABLE habitaciones ADD COLUMN nombre VARCHAR(60);

ALTER TABLE reservas CHANGE COLUMN id_habitacion id_habitaciones INTEGER;
ALTER TABLE reservas MODIFY COLUMN fecha_salida DATE;
ALTER TABLE habitaciones ADD capacidad INT;
ALTER TABLE reservas

--Cambios en la tabla clientes para reservas con mas de un cliente
DROP COLUMN id_cliente;
CREATE TABLE reservas_clientes (
    id_reserva_cliente INT PRIMARY KEY AUTO_INCREMENT,
    id_reserva INT NOT NULL,
    id_cliente INT NOT NULL,
    es_titular BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);
CREATE INDEX idx_reserva_cliente ON reservas_clientes(id_reserva, id_cliente);
ALTER TABLE reservas
ADD COLUMN numero_personas INT NOT NULL DEFAULT 1;

