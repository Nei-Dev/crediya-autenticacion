CREATE TABLE rol
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100)                NOT NULL,
    apellido            VARCHAR(100)                NOT NULL,
    id_rol              BIGINT                      NOT NULL,
    correo_electronico  VARCHAR(150)                NOT NULL UNIQUE,
    salario_base        INTEGER,
    fecha_nacimiento    DATE,
    direccion           VARCHAR(255),
    telefono            VARCHAR(20),
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id)
);