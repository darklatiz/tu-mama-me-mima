-- Tabla esquema_pagos
CREATE TABLE esquema_pagos
(
    id               SERIAL PRIMARY KEY,
    nombre           VARCHAR(255)   NOT NULL,
    numero_pagos     INT            NOT NULL,
    frecuencia_cobro VARCHAR(50)    NOT NULL,
    tasa             NUMERIC(10, 2) NOT NULL,
    habilitado       BOOLEAN        NOT NULL
);

-- Tabla condiciones_regla
CREATE TABLE condiciones_regla
(
    id         SERIAL PRIMARY KEY,
    clave      VARCHAR(255) NOT NULL,
    valor      VARCHAR(255) NOT NULL,
    operador   VARCHAR(10)  NOT NULL,
    habilitado BOOLEAN      NOT NULL
);

-- Tabla de relación muchos a muchos entre esquema_pagos y condiciones_regla
CREATE TABLE esquema_condicion
(
    id_esquema   BIGINT NOT NULL,
    id_condicion BIGINT NOT NULL,
    PRIMARY KEY (id_esquema, id_condicion),
    CONSTRAINT fk_esquema FOREIGN KEY (id_esquema) REFERENCES esquema_pagos (id) ON DELETE CASCADE,
    CONSTRAINT fk_condicion FOREIGN KEY (id_condicion) REFERENCES condiciones_regla (id) ON DELETE CASCADE
);


-- Crear tabla clientes
CREATE TABLE clientes
(
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(255) NOT NULL,
    fecha_nacimiento    DATE         NOT NULL,
    fecha_registro      TIMESTAMP    NOT NULL,
    fecha_actualizacion TIMESTAMP,
    ap_paterno          VARCHAR(255) NOT NULL,
    ap_materno          VARCHAR(255) NOT NULL
);

-- Crear tabla linea_creditos
CREATE TABLE linea_creditos
(
    id                  BIGSERIAL PRIMARY KEY,
    id_cliente          BIGINT         NOT NULL,
    monto_asignado      NUMERIC(10, 2) NOT NULL,
    fecha_registro      TIMESTAMP      NOT NULL,
    fecha_actualizacion TIMESTAMP,
    CONSTRAINT fk_clientes FOREIGN KEY (id_cliente) REFERENCES clientes (id)
);

-- Crear tabla compras
CREATE TABLE compras
(
    id              BIGSERIAL PRIMARY KEY,
    id_cliente      BIGINT         NOT NULL,
    id_esquema_pago BIGINT         NOT NULL,
    fecha_compra    TIMESTAMP      NOT NULL,
    monto           NUMERIC(10, 2) NOT NULL,
    monto_comision  NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_compras_clientes FOREIGN KEY (id_cliente) REFERENCES clientes (id),
    CONSTRAINT fk_compras_esquema_pagos FOREIGN KEY (id_esquema_pago) REFERENCES esquema_pagos (id)
);

-- Crear tabla plazos
CREATE TABLE plazos
(
    id         BIGSERIAL PRIMARY KEY,
    id_compra  BIGINT         NOT NULL,
    monto      NUMERIC(10, 2) NOT NULL,
    num_pago   INT            NOT NULL,
    fecha_pago DATE           NOT NULL,
    CONSTRAINT fk_plazos_compras FOREIGN KEY (id_compra) REFERENCES compras (id)
);

-- Inserts para esquema_pagos
INSERT INTO esquema_pagos (nombre, numero_pagos, frecuencia_cobro, tasa, habilitado)
VALUES ('Esquema 1', 12, 'Mensual', 1.5, true),
       ('Esquema 2', 6, 'Quincenal', 2.0, true),
       ('Esquema 3', 24, 'Anual', 1.2, false);

-- Inserts para condiciones_regla
INSERT INTO condiciones_regla (clave, valor, operador, habilitado)
VALUES ('edad', '25', '>=', true),
       ('ingreso', '50000', '>=', true),
       ('antiguedad', '2', '>=', true);

-- Inserts para esquema_condicion (relación muchos a muchos)
INSERT INTO esquema_condicion (id_esquema, id_condicion)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 1);

-- Inserts para clientes
INSERT INTO clientes (nombre, fecha_nacimiento, fecha_registro, fecha_actualizacion, ap_paterno, ap_materno)
VALUES ('Juan Pérez', '1990-05-15', '2023-01-01', NULL, 'Pérez', 'López'),
       ('María García', '1985-10-10', '2022-11-15', NULL, 'García', 'Martínez'),
       ('Carlos Sánchez', '2000-07-20', '2023-05-20', NULL, 'Sánchez', 'Hernández');

-- Inserts para linea_creditos
INSERT INTO linea_creditos (id_cliente, monto_asignado, fecha_registro, fecha_actualizacion)
VALUES (1, 10000.00, '2023-01-01', NULL),
       (2, 15000.00, '2022-11-15', NULL),
       (3, 5000.00, '2023-05-20', NULL);

-- Inserts para compras
INSERT INTO compras (id_cliente, id_esquema_pago, fecha_compra, monto, monto_comision)
VALUES (1, 1, '2023-06-15', 1000.00, 50.00),
       (2, 2, '2023-07-10', 2000.00, 100.00),
       (3, 3, '2023-08-01', 500.00, 20.00);

-- Inserts para plazos
INSERT INTO plazos (id_compra, monto, num_pago, fecha_pago)
VALUES (1, 100.00, 1, '2023-07-15'),
       (1, 100.00, 2, '2023-08-15'),
       (2, 200.00, 1, '2023-08-10'),
       (2, 200.00, 2, '2023-09-10'),
       (3, 50.00, 1, '2023-09-01');
