-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-01-2024 a las 19:30:08
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `docsecurepro`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivos`
--

CREATE TABLE `archivos` (
  `id_archivo` tinyint(3) UNSIGNED NOT NULL,
  `nombre_archivo` varchar(60) NOT NULL,
  `ruta` varchar(100) NOT NULL,
  `carpeta_no` tinyint(1) DEFAULT NULL,
  `id_usuarios` tinyint(3) UNSIGNED NOT NULL,
  `id_carpeta` tinyint(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `eventos`
--

CREATE TABLE `eventos` (
  `id_evento` tinyint(3) UNSIGNED NOT NULL,
  `nombre_evento` varchar(60) DEFAULT NULL,
  `id_archivo` tinyint(3) UNSIGNED DEFAULT NULL,
  `id_usuarios` tinyint(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logg`
--

CREATE TABLE `logg` (
  `id_log` tinyint(3) UNSIGNED NOT NULL,
  `fecha_hora` datetime NOT NULL DEFAULT current_timestamp(),
  `id_usuario` tinyint(3) UNSIGNED NOT NULL,
  `tipo_operacion` tinyint(3) UNSIGNED NOT NULL,
  `id_archivos` tinyint(3) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` tinyint(3) UNSIGNED NOT NULL,
  `nombre_rol` varchar(30) NOT NULL,
  `descripcion_rol` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre_rol`, `descripcion_rol`) VALUES
(1, 'SuperAdmin', 'Usuario que tiene acceso a todas las funciones disponibles, como cambiar datos, borrar o añadir otros usuarios, añadir o cambiar roles de otros usuarios, ETC.'),
(2, 'Directivo', ' Trabajador de la empresa que posee acceso a todos los datos de la aplicación pero no al nivel del superadmin.'),
(3, 'Representante', 'Es el usuario representante de un grupo/cantante, no tiene acceso a ninguna función relevante a la administración de la aplicación, pero puede consultar algunos archivos relacionados con su id de usuario o eventos que tengan en relación con el.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_operacion`
--

CREATE TABLE `tipo_operacion` (
  `id_tipo_operacion` tinyint(3) UNSIGNED NOT NULL,
  `tipo_operacion` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_operacion`
--

INSERT INTO `tipo_operacion` (`id_tipo_operacion`, `tipo_operacion`) VALUES
(4, 'Creacion de carpeta'),
(2, 'Descarga de archivo'),
(5, 'Eliminacion de carpeta'),
(3, 'Renombre de archivo'),
(6, 'Renombre de carpeta'),
(1, 'Subida de archivo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuarios` tinyint(3) UNSIGNED NOT NULL,
  `dni` char(9) DEFAULT NULL,
  `nombre_usuario` varchar(60) NOT NULL,
  `contrasenya` varchar(50) NOT NULL,
  `correo` varchar(50) NOT NULL,
  `id_rol` tinyint(3) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `archivos`
--
ALTER TABLE `archivos`
  ADD PRIMARY KEY (`id_archivo`),
  ADD KEY `id_usuarios` (`id_usuarios`),
  ADD KEY `id_carpeta` (`id_carpeta`);

--
-- Indices de la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD PRIMARY KEY (`id_evento`),
  ADD KEY `id_archivo` (`id_archivo`),
  ADD KEY `id_usuarios` (`id_usuarios`);

--
-- Indices de la tabla `logg`
--
ALTER TABLE `logg`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `tipo_operacion` (`tipo_operacion`),
  ADD KEY `id_archivos` (`id_archivos`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `tipo_operacion`
--
ALTER TABLE `tipo_operacion`
  ADD PRIMARY KEY (`id_tipo_operacion`),
  ADD UNIQUE KEY `tipo_operacion` (`tipo_operacion`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuarios`),
  ADD UNIQUE KEY `correo` (`correo`),
  ADD KEY `id_rol` (`id_rol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `archivos`
--
ALTER TABLE `archivos`
  MODIFY `id_archivo` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id_evento` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `logg`
--
ALTER TABLE `logg`
  MODIFY `id_log` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipo_operacion`
--
ALTER TABLE `tipo_operacion`
  MODIFY `id_tipo_operacion` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuarios` tinyint(3) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `archivos`
--
ALTER TABLE `archivos`
  ADD CONSTRAINT `archivos_ibfk_1` FOREIGN KEY (`id_usuarios`) REFERENCES `usuarios` (`id_usuarios`),
  ADD CONSTRAINT `archivos_ibfk_2` FOREIGN KEY (`id_carpeta`) REFERENCES `archivos` (`id_archivo`);

--
-- Filtros para la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD CONSTRAINT `eventos_ibfk_1` FOREIGN KEY (`id_archivo`) REFERENCES `archivos` (`id_archivo`),
  ADD CONSTRAINT `eventos_ibfk_2` FOREIGN KEY (`id_usuarios`) REFERENCES `usuarios` (`id_usuarios`);

--
-- Filtros para la tabla `logg`
--
ALTER TABLE `logg`
  ADD CONSTRAINT `logg_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuarios`),
  ADD CONSTRAINT `logg_ibfk_2` FOREIGN KEY (`tipo_operacion`) REFERENCES `tipo_operacion` (`id_tipo_operacion`),
  ADD CONSTRAINT `logg_ibfk_3` FOREIGN KEY (`id_archivos`) REFERENCES `archivos` (`id_archivo`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
