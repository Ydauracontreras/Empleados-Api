package ar.com.ada.api.empleados.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.empleados.entidades.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}