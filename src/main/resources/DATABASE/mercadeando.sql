-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 08-01-2021 a las 14:49:20
-- Versión del servidor: 10.4.14-MariaDB
-- Versión de PHP: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mercadeando`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `apellido` varchar(255) NOT NULL,
  `cedula` varchar(255) DEFAULT NULL,
  `ciudad` varchar(255) DEFAULT NULL,
  `departamento` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `activo`, `apellido`, `cedula`, `ciudad`, `departamento`, `direccion`, `nombre`) VALUES
(1, b'1', 'Lopez', '112777001', 'Cartago', 'VALLE_DEL_CAUCA', 'Crra 1 N # 54-26', 'Fernando'),
(2, b'1', 'Castaño', '112777002', 'Amalfi', 'ANTIOQUIA', 'Crra 2 N # 54-26', 'Carlos'),
(3, b'1', 'Ochoa Vásquez', '112777003', 'Rionegro', 'ANTIOQUIA', 'Crra 3 N # 54-26', 'Fabio'),
(4, b'1', 'Rodríguez Gacha', '112777004', 'Pacho', 'CUNDINAMARCA', 'Crra 4 N # 54-26', 'Gonzalo'),
(5, b'1', 'Lehder', '112777005', 'Armenia', 'QUINDIO', 'Crra 5 N # 54-26', 'Carlos'),
(6, b'1', 'Gaviria', '112777006', 'Pereira', 'RISARALDA', 'Crra 6 N # 54-26', 'Gustavo'),
(7, b'1', 'Ochoa Vásquez', '112777007', 'Medellin', 'ANTIOQUIA', 'Crra 7 N # 54-26', 'Jorge Luis'),
(8, b'1', 'Ochoa Vásquez', '112777008', 'Medellin', 'ANTIOQUIA', 'Crra 8 N # 54-26', 'Juan David'),
(9, b'1', 'Ochoa Vásquez', '112777009', 'Medellin', 'ANTIOQUIA', 'Crra 9 N # 54-26', 'Fabio'),
(10, b'1', 'Blanco Restrepo', '112777010', 'Cartagena', 'BOLIVAR', 'Crra 10 N # 54-26', 'Ana Griselda');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden`
--

CREATE TABLE `orden` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `precio` decimal(19,2) DEFAULT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `pago_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `orden`
--

INSERT INTO `orden` (`id`, `activo`, `created_at`, `estado`, `fecha`, `precio`, `cliente_id`, `pago_id`, `user_id`) VALUES
(2, b'1', '2021-01-08 08:48:04', '1', '2021-01-08 08:48:04', '1000.00', 1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `orden_producto`
--

CREATE TABLE `orden_producto` (
  `id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `orden_producto`
--

INSERT INTO `orden_producto` (`id`, `cantidad`, `order_id`, `producto_id`) VALUES
(1, 1, 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `id` bigint(20) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `metodo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `pago`
--

INSERT INTO `pago` (`id`, `fecha`, `metodo`) VALUES
(1, '2021-01-15 08:47:19', 'EFECTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `descripcion` longtext DEFAULT NULL,
  `estado` bit(1) DEFAULT NULL,
  `foto` varchar(2000) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `precio` decimal(19,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `activo`, `cantidad`, `created_at`, `descripcion`, `estado`, `foto`, `nombre`, `precio`) VALUES
(1, b'1', 12, '2020-11-24 08:07:04', 'EMPANADAS', b'1', NULL, 'EMPANADAS', '1000.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `refresh_token`
--

CREATE TABLE `refresh_token` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `refresh_token`
--

INSERT INTO `refresh_token` (`id`, `created_at`, `token`) VALUES
(1, '2021-01-07 15:03:05', 'a5b4883b-60ea-4d79-813b-1987e39becea'),
(2, '2021-01-07 15:10:04', 'e3d1b308-782f-44e7-a923-f09f6ecaf7f0'),
(3, '2021-01-07 15:17:57', 'dcb8f1f1-f228-4dc8-970e-412d8cdb7347'),
(4, '2021-01-07 15:20:50', '13748d8f-99fb-433f-9b43-450778dfdaab'),
(5, '2021-01-07 15:45:59', 'f04517bc-bd1f-4df6-ae32-091818ca8dff');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `token`
--

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `activo`, `created_at`, `email`, `password`, `username`) VALUES
(1, b'1', '2021-01-07 15:00:24', 'cristianmarint@mail.com', '$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG', 'cristianmarint');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_trfs6xemfuo1u29blh0jw3ekl` (`cedula`);

--
-- Indices de la tabla `orden`
--
ALTER TABLE `orden`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKijfuuiwohsilb5l5qcsv6qsdg` (`cliente_id`),
  ADD KEY `FKc0ga001jy0tpfwgkd7sqrd66y` (`pago_id`),
  ADD KEY `FKff7bwino1bt2d34ih31bg5kth` (`user_id`);

--
-- Indices de la tabla `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2isn3p45khntb0qgaw5be4msf` (`order_id`),
  ADD KEY `FKl1uaeyo2ksdiw0ls68ckwptlc` (`producto_id`);

--
-- Indices de la tabla `pago`
--
ALTER TABLE `pago`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `refresh_token`
--
ALTER TABLE `refresh_token`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `orden`
--
ALTER TABLE `orden`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `orden_producto`
--
ALTER TABLE `orden_producto`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `pago`
--
ALTER TABLE `pago`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `refresh_token`
--
ALTER TABLE `refresh_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `token`
--
ALTER TABLE `token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `orden`
--
ALTER TABLE `orden`
  ADD CONSTRAINT `FKc0ga001jy0tpfwgkd7sqrd66y` FOREIGN KEY (`pago_id`) REFERENCES `pago` (`id`),
  ADD CONSTRAINT `FKff7bwino1bt2d34ih31bg5kth` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKijfuuiwohsilb5l5qcsv6qsdg` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`);

--
-- Filtros para la tabla `orden_producto`
--
ALTER TABLE `orden_producto`
  ADD CONSTRAINT `FK2isn3p45khntb0qgaw5be4msf` FOREIGN KEY (`order_id`) REFERENCES `orden` (`id`),
  ADD CONSTRAINT `FKl1uaeyo2ksdiw0ls68ckwptlc` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `token`
--
ALTER TABLE `token`
  ADD CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
