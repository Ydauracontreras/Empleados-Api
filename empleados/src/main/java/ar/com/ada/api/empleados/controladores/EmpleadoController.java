package ar.com.ada.api.empleados.controladores;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.empleados.entidades.AltaEmpleado;
import ar.com.ada.api.empleados.entidades.Empleado;
import ar.com.ada.api.empleados.entidades.AltaEmpleado.AltaEmpleadoResultEnum;
import ar.com.ada.api.empleados.entidades.Empleado.EmpleadoEstadoEnum;
import ar.com.ada.api.empleados.models.request.EmpleadoRequest;
import ar.com.ada.api.empleados.models.request.EmpleadoSueldoNuevoRequest;
import ar.com.ada.api.empleados.models.response.EmpleadosResponse;
import ar.com.ada.api.empleados.models.response.GenericResponse;
import ar.com.ada.api.empleados.servicios.*;

@RestController
@CrossOrigin("*")
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    CategoriaService categoriaService;

    // Crea un Empleado
    @PostMapping("/empleados")
    public ResponseEntity<?> postEmpleados(@RequestBody EmpleadoRequest empleadoInfo) {
        Empleado nuevo = new Empleado();
        nuevo.setNombre(empleadoInfo.nombre);
        nuevo.setEdad(empleadoInfo.edad);
        nuevo.setSueldo(empleadoInfo.sueldo);
        nuevo.setFechaAlta(new Date());
        nuevo.setCategoria(categoriaService.buscarPorId(empleadoInfo.categoriaId));
        nuevo.setEstadoId(EmpleadoEstadoEnum.ACTIVO);
        AltaEmpleado alta = empleadoService.crearEmpleado(nuevo);
        GenericResponse r = new GenericResponse();
        if (alta.getResultado() == AltaEmpleadoResultEnum.REALIZADA) {
            r.isOk = true;
            r.id = nuevo.getEmpleadoId();
            r.message = "Creaste un empleado con éxito.";
            return ResponseEntity.ok(r);
        } else {
            r.isOk = false;
            r.message = "No pudiste crear el empleado";
            return ResponseEntity.badRequest().body(r);

        }
    }

    @GetMapping("/empleados")
    public List<Empleado> getEmpleados(@RequestParam(value = "nombre", required = false) String nombre) {
        return empleadoService.obtenerEmpleados();

    }

    @GetMapping("empleados/filtrados")
    public List<EmpleadosResponse> getEmpleadosFiltrados() {
        List<EmpleadosResponse> listaFiltrada = new ArrayList<>();
        List<Empleado> listas = empleadoService.obtenerEmpleados();
        for (Empleado empleado : listas) {
            EmpleadosResponse response = new EmpleadosResponse();
            response.id = empleado.getEmpleadoId();
            response.nombre = empleado.getNombre();
            response.edad = empleado.getEdad();
            response.sueldo = empleado.getSueldo();
            response.nombreCategoria = empleado.getCategoria().getNombre();

            listaFiltrada.add(response);
        }
        return listaFiltrada;

    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable int id) {
        Empleado e = empleadoService.obtenerPorId(id);
        if (e == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // Otra forma de hacer el notFount
            // return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(e);
    }

    @GetMapping("/empleados/categorias/{id_categoria}")
    public ResponseEntity<List<Empleado>> getEmpleadosByCategoriaId(@PathVariable(value = "id_categoria") int id) {
        List<Empleado> empleados = categoriaService.buscarPorId(id).getEmpleados();
        return ResponseEntity.ok(empleados);
        // return empleadoService.buscarPorIdCategoria(id);
    }

    // Actualiza excepto sueldo y estado
    @PutMapping("/empleados/{id}")
    public ResponseEntity<?> postEmpleados(@PathVariable int id, @RequestBody EmpleadoSueldoNuevoRequest empleado) {
        Empleado empleadoModificado = empleadoService.obtenerPorId(id);
        if (empleadoModificado == null) {
            return ResponseEntity.notFound().build();
        }
        empleadoModificado.setSueldo(empleado.sueldoNuevo);
        GenericResponse r = new GenericResponse();
        boolean resultado = empleadoService.crearEmpleados(empleadoModificado);
        if (resultado) {
            r.isOk = true;
            r.id = empleadoModificado.getEmpleadoId();
            r.message = "Modificaste un empleado con éxito.";
            return ResponseEntity.ok(r);
        } else {
            r.isOk = false;
            r.message = "No pudiste Modificar el empleado";
            return ResponseEntity.badRequest().body(r);

        }

        /*
         * GenericResponse r = new GenericResponse();
         *
         * Empleado empleadoOriginal = empleadoService.buscarPorId(id);
         *
         * if (empleadoOriginal == null) { return new
         * ResponseEntity<>(HttpStatus.NOT_FOUND); } boolean resultado = false;
         * resultado = empleadoService.actualizarEnpleado(empleadoOriginal, empleado);
         *
         * if (resultado) { r.isOk = true; r.id = id; r.message =
         * "Empleado actualizado con éxito."; return ResponseEntity.ok(r); } else {
         * r.isOk = false; r.message = "No se pudo actualizar el Empleado."; return
         * ResponseEntity.badRequest().body(r);
         *
         * }
         */

    }

    // Actualiza sueldo
    @PutMapping("/empleados/{id}/sueldos")
    public ResponseEntity<?> postEmpleadosSueldo(@PathVariable int id, @RequestBody Empleado req) {
        GenericResponse r = new GenericResponse();

        Empleado empleadoOriginal = empleadoService.obtenerPorId(id);

        if (empleadoOriginal == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean resultado = false;
        resultado = empleadoService.actualizarSueldoEnpleado(empleadoOriginal, req);

        if (resultado) {
            r.isOk = true;
            r.id = id;
            r.message = "Empleado actualizado con éxito.";
            return ResponseEntity.ok(r);
        } else {
            r.isOk = false;
            r.message = "No se pudo actualizar el Empleado.";
            return ResponseEntity.badRequest().body(r);

        }

    }

    /*
     * @PutMapping("/empleados/{id}/baja") public ResponseEntity<?>
     * deleteEmpleados(@PathVariable int id, @RequestBody Empleado req) {
     * GenericResponse r = new GenericResponse();
     *
     * Empleado empleadoOriginal = empleadoService.buscarPorId(id);
     *
     * if (empleadoOriginal == null) { return new
     * ResponseEntity<>(HttpStatus.NOT_FOUND); } boolean resultado = false;
     * resultado = empleadoService.EliminarLogicamente(empleadoOriginal, req); if
     * (resultado) { r.isOk = true; r.id = id; r.message =
     * "Empleado fue dado de baja con éxito."; return ResponseEntity.ok(r); } return
     * null;
     *
     * }
     */
    @DeleteMapping("/empleados/{id}/baja")
    public ResponseEntity<?> deleteEmpleados(@PathVariable int id) {
        GenericResponse r = new GenericResponse();
        Empleado empleadoDeBaja = empleadoService.obtenerPorId(id);
        if (empleadoDeBaja == null) {
            return ResponseEntity.notFound().build();
        }
        empleadoDeBaja.setFechaBaja(new Date());
        empleadoDeBaja.setEstadoId(EmpleadoEstadoEnum.ACTIVO);
        empleadoService.grabar(empleadoDeBaja);

        boolean resultado = empleadoService.crearEmpleados(empleadoDeBaja);
        if (resultado) {
            r.isOk = true;
            r.id = empleadoDeBaja.getEmpleadoId();
            r.message = "Eliminaste un empleado con éxito.";
            return ResponseEntity.ok(r);
        } else {
            r.isOk = false;
            r.message = "No pudiste Modificar el empleado";
            return ResponseEntity.badRequest().body(r);
        }
    }

}
