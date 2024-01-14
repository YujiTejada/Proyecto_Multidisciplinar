CREATE DATABASE DocSecurePro;   

USE DocSecurePro;

CREATE TABLE Tipo_operacion(
    id_tipo_operacion TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion VARCHAR (30) NOT NULL UNIQUE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE Roles(
    id_rol TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR (30) NOT NULL,
    descripcion_rol VARCHAR (300) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABlE Usuarios (
    id_usuarios TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    dni CHAR (9) NULL,
    nombre_usuario VARCHAR (60) NOT NULL,
    contrasenya VARCHAR (50) NOT NULL,
    correo VARCHAR (50) NOT NULL UNIQUE,
    id_rol TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES Roles (id_rol)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE Archivos (
    id_archivo TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR (60) NOT NULL,
    ruta VARCHAR (100) NOT NULL,
    carpeta_no BOOLEAN NULL,
    id_usuarios TINYINT UNSIGNED NOT NULL,
    id_carpeta TINYINT UNSIGNED NULL,
    FOREIGN KEY (id_usuarios) REFERENCES Usuarios (id_usuarios),
    FOREIGN KEY (id_carpeta) REFERENCES Archivos (id_archivo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE Eventos(
    id_evento TINYINT UNSIGNED NULL AUTO_INCREMENT PRIMARY KEY,
    nombre_evento VARCHAR (60) NULL,
    id_archivo TINYINT UNSIGNED NULL,
    id_usuarios TINYINT UNSIGNED NULL,
    FOREIGN KEY (id_archivo) REFERENCES Archivos (id_archivo),
    FOREIGN KEY (id_usuarios) REFERENCES Usuarios (id_usuarios)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE Logg(
    id_log TINYINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario TINYINT UNSIGNED NOT NULL,
    tipo_operacion TINYINT UNSIGNED NOT NULL,
    id_archivos TINYINT UNSIGNED NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios (id_usuarios),
    FOREIGN KEY (tipo_operacion) REFERENCES Tipo_operacion (id_tipo_operacion),
    FOREIGN KEY (id_archivos) REFERENCES Archivos (id_archivo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;