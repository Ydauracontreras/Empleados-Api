package ar.com.ada.api.empleados.servicios;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.empleados.entidades.AltaEmpleado;
import ar.com.ada.api.empleados.entidades.Empleado;
import ar.com.ada.api.empleados.entidades.AltaEmpleado.AltaEmpleadoResultEnum;
import ar.com.ada.api.empleados.entidades.Empleado.EmpleadoEstadoEnum;
import ar.com.ada.api.empleados.repositorios.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    /**
     * Cambio el crear empleado, ahora devuelve otro objeto, que tiene el resultado
     * de la operacion de ALTA de un empleado, tambien tiene el empleado de
     * referencia(sirve para los casos donde se pasan todo por parametro y devolver
     * el objeto)
     * 
     * @param empleado
     * @return
     */

    public boolean crearEmpleados(Empleado empleado) {
        grabar(empleado);
        return true;
    }

    public AltaEmpleado crearEmpleado(Empleado empleado) {
        AltaEmpleado alta = new AltaEmpleado();

        EmpleadoValidationType resultadoValidacion = grabar(empleado);

        // Aca en vez de usar IF, se usa el operador ternario "?"
        // Donde se expresa asi: variable = (condicion)?retorno verdadero:retorno falso;

        /**
         * if (resultadoValidacion == EmpleadoValidationType.EMPLEADO_OK)
         * alta.setResultado(AltaEmpleadoResultEnum.REALIZADA); else
         * alta.setResultado(AltaEmpleadoResultEnum.RECHAZADA);
         */
        alta.setResultado(resultadoValidacion == EmpleadoValidationType.EMPLEADO_OK ? AltaEmpleadoResultEnum.REALIZADA
                : AltaEmpleadoResultEnum.RECHAZADA);

        alta.setMotivo(resultadoValidacion);
        alta.setEmpleado(empleado);

        return alta;
    }

    public List<Empleado> obtenerEmpleados() {
        return empleadoRepository.findAll();
    }

    public Empleado obtenerPorId(Integer id) {
        Optional<Empleado> c = empleadoRepository.findById(id);
        if (c.isPresent())
            return c.get();
        return null;
    }

    public EmpleadoValidationType grabar(Empleado empleado) {

        EmpleadoValidationType resultado = verificarEmpleado(empleado);
        if (resultado != EmpleadoValidationType.EMPLEADO_OK)
            return resultado;

        empleadoRepository.save(empleado);

        return EmpleadoValidationType.EMPLEADO_OK;

    }

    public enum EmpleadoValidationType {

        EMPLEADO_OK, EMPLEADO_DUPLICADO, EMPLEADO_INVALIDO, SUELDO_NULO, EDAD_INVALIDA, NOMBRE_INVALIDO, DNI_INVALIDO,
        EMPLEADO_DATOS_INVALIDOS

    }

    /**
     * Verifica que el nombre no est� nulo, La edad sea mayor a 0, El sueldo sea
     * mayor a 0, El estado, la fecha de alta y baja no est�n en nulo.
     * 
     * @param empleado
     * @return
     */

    public EmpleadoValidationType verificarEmpleado(Empleado empleado) {

        if (empleado.getNombre() == null)
            return EmpleadoValidationType.NOMBRE_INVALIDO;

        if (empleado.getEdad() <= 0)
            return EmpleadoValidationType.EDAD_INVALIDA;

        if (empleado.getSueldo().compareTo(new BigDecimal(0)) <= 0)
            return EmpleadoValidationType.SUELDO_NULO;
        if (empleado.getEstadoId() == EmpleadoEstadoEnum.DESCONOCIDO)
            return EmpleadoValidationType.EMPLEADO_DATOS_INVALIDOS; // ACA GENERICO
        if (empleado.getFechaAlta() == null)
            return EmpleadoValidationType.EMPLEADO_DATOS_INVALIDOS;
        if (empleado.getDni() <= 0)
            return EmpleadoValidationType.DNI_INVALIDO;

        Empleado e = empleadoRepository.findByDni(empleado.getDni());
        if (e != null) {
            if (empleado.getEmpleadoId() != 0) {
                if ((empleado.getEmpleadoId() != e.getEmpleadoId())) {
                    return EmpleadoValidationType.EMPLEADO_DUPLICADO;
                } else {
                    return EmpleadoValidationType.EMPLEADO_OK;
                }
            } else
                return EmpleadoValidationType.EMPLEADO_DUPLICADO;

        }
        return EmpleadoValidationType.EMPLEADO_OK;
    }

    public void actualizarEnpleado(Empleado empleado) {
        /*
         * empleadoOriginal.setNombre(empleadoNuevaInfo.getNombre());
         * empleadoOriginal.setEdad(empleadoNuevaInfo.getEdad());
         * empleadoOriginal.setCategoria(empleadoNuevaInfo.getCategoria());
         * empleadoOriginal.setFechaAlta(empleadoNuevaInfo.getFechaAlta());
         * empleadoRepository.save(empleadoOriginal); return true;
         */
    }

    public boolean actualizarSueldoEnpleado(Empleado empleadoOriginal, Empleado empleadoNuevaInfo) {
        empleadoOriginal.setSueldo(empleadoNuevaInfo.getSueldo());
        empleadoRepository.save(empleadoOriginal);
        return true;

    }

    public boolean EliminarLogicamente(Empleado empleadoOriginal, Empleado empleadoNuevaInfo) {
        empleadoOriginal.setEstadoId(empleadoNuevaInfo.getEstadoId());
        empleadoOriginal.setFechaBaja(new Date());
        empleadoRepository.save(empleadoOriginal);
        return true;

    }
}