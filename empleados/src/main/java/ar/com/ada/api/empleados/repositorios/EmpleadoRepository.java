package ar.com.ada.api.empleados.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.empleados.entidades.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    List<Empleado> findAllByCategoriaId(Integer idCategoria);

    // codigo

    Empleado findByNombre(String nombre);

    Empleado findByDni(int id);

}
