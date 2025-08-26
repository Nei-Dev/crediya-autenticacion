CREATE TABLE rol
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre             VARCHAR(100)               NOT NULL,
    apellido           VARCHAR(100)               NOT NULL,
    id_rol             BIGINT REFERENCES rol (id) NOT NULL,
    correo_electronico VARCHAR(150)               NOT NULL UNIQUE,
    salario_base       NUMERIC,
    fecha_nacimiento   DATE,
    direccion          VARCHAR(255),
    telefono           VARCHAR(20)
);