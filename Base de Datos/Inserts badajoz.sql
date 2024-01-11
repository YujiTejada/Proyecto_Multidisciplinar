USE DocSecurePro;

INSERT INTO Roles(nombre_rol, descripcion_rol)
	VALUES('SuperAdmin', 'Usuario que tiene acceso a todas las funciones disponibles, como cambiar datos, borrar o añadir otros usuarios, añadir o cambiar roles de otros usuarios, ETC.');

INSERT INTO Roles(nombre_rol, descripcion_rol)
    VALUES('Directivo', ' Trabajador de la empresa que posee acceso a todos los datos de la aplicación pero no al nivel del superadmin.');

INSERT INTO Roles(nombre_rol, descripcion_rol)
    VALUES ('Representante', 'Es el usuario representante de un grupo/cantante, no tiene acceso a ninguna función relevante a la administración de la aplicación, pero puede consultar algunos archivos relacionados con su id de usuario o eventos que tengan en relación con el.');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Subida de archivo');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Descarga de archivo');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Renombre de archivo');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Creacion de carpeta');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Eliminacion de carpeta');

INSERT INTO tipo_operacion(tipo_operacion)
    VALUES ('Renombre de carpeta');