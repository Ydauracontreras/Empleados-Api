package ar.com.ada.api.empleados.entidades.calculos;

import java.math.BigDecimal;

import ar.com.ada.api.empleados.entidades.Empleado;

/**
 * ueldoCalculator Interface Strategy para calculo de sueldo
 */
public interface SueldoCalculator {

    BigDecimal calcularSueldo(Empleado empleado);

}