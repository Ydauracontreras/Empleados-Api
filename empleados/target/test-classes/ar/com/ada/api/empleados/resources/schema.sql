
--CREO LA TABLA , SI NO EXISTE
CREATE TABLE `categoria` (
  `categoria_id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `sueldo_base` decimal(14,2) NOT NULL,
  PRIMARY KEY (`categoria_id`)
);

CREATE TABLE `estado` (
  `estado_id` int(11) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`estado_id`)
);

--CREO LA TABLA , SI NO EXISTE
CREATE TABLE `empleado` (
  `empleado_id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(240) NOT NULL,
  `edad` int(11) NOT NULL,
  `categoria_id` int(11) NOT NULL,
  `sueldo` decimal(14,2) NOT NULL,
  `fecha_alta` datetime NOT NULL,
  `fecha_baja` datetime NOT NULL,
  `estado_id` int(11) NOT NULL,
  `dni` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`empleado_id`),
  CONSTRAINT FK_empleado_categoria_id FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`),
  CONSTRAINT FK_empleado_estado_id FOREIGN KEY (`estado_id`) REFERENCES `estado` (`estado_id`)
);

