package ar.com.ada.api.empleados.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import ar.com.ada.api.empleados.entidades.Categoria;
import ar.com.ada.api.empleados.entidades.Empleado;
import ar.com.ada.api.empleados.models.response.CategoriasNombresResponse;
import ar.com.ada.api.empleados.models.response.GenericResponse;
import ar.com.ada.api.empleados.servicios.CategoriaService;

@RestController
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @PostMapping("/categorias")
    public ResponseEntity<?> postCategoria(@RequestBody Categoria categoria) {
        GenericResponse r = new GenericResponse();
        boolean resultado = categoriaService.CrearCategoria(categoria);
        if (resultado) {
            r.isOk = true;
            r.id = categoria.getCategoriaId();
            r.message = "Creaste una categoria con éxito.";
            return ResponseEntity.ok(r);
        } else {
            r.isOk = false;
            r.message = "No se puedo crear la categoria";
            return ResponseEntity.badRequest().body(r);
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias(
            @RequestParam(value = "nombre", required = false) String nombre) {
        return ResponseEntity.ok(categoriaService.obtenerCategorias());

    }

    @GetMapping("categorias/{id}")
    public ResponseEntity<Categoria> getCategoriasById(@PathVariable int id) {
        Categoria c = categoriaService.buscarPorId(id);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/categorias/sueldos-nuevos")
    public ResponseEntity<List<Empleado>> getSueldosNuevos() {

        return ResponseEntity.ok(categoriaService.calcularProximosSueldos());

    }

    @GetMapping("/categorias/sueldos-actuales")
    public ResponseEntity<List<Empleado>> getSueldosActuales() {

        return ResponseEntity.ok(categoriaService.obtenerSueldosActuales());

    }

    @GetMapping("/categorias/vacias")
    public ResponseEntity<List<Categoria>> getCategoriasSinEmpleados() {

        return ResponseEntity.ok(categoriaService.obtenerCategoriasSinEmpleados());

    }

    @GetMapping("/categorias/nombres")
    public ResponseEntity<CategoriasNombresResponse> getCategoriasNombres() {

        CategoriasNombresResponse r = new CategoriasNombresResponse();

        r.nombres = categoriaService.obtenerNombresCategorias();
        return ResponseEntity.ok(r);

    }
}
