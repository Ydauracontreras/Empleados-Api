package ar.com.ada.api.empleados;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.com.ada.api.empleados.entidades.Categoria;
import ar.com.ada.api.empleados.servicios.CategoriaService;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	CategoriaService categoriaService;

	@Test
	void contextLoads() {
	}

	@Test
	void estaLaCategoria1() {
		Categoria categoria1 = categoriaService.buscarPorId(1);
		assertNotNull(categoria1);
	}

	// 1: Usando Streams(sin REPOS): Traer todas las categorias que tengan la letra
	// 'V'(case insensitive) y verificar que haya alguna
	@Test
	void verificarCategoriasConLetraV() {
		List<Categoria> categorias = categoriaService.listarCategortias().stream()
				.filter(c -> c.getNombre().contains("V") || c.getNombre().contains("v")).collect(Collectors.toList());
		assertTrue(categorias.size() > 0);
	}

	// 2: Sumar todos los importes de los sueldos base y verificar qu sea mayor a 0

	@Test
	void calcularSumaSueldosBaseYVerificarMayorA0() {
		List<BigDecimal> sueldosSB = categoriaService.listarCategortias().stream().map(cat -> cat.getSueldoBase())
				.collect(Collectors.toList());

		BigDecimal sumaSueldosSB = sueldosSB.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

		assertTrue(sumaSueldosSB.compareTo(new BigDecimal(0)) == 1);
	}

	// 3: Obtener la lista de sueldos base y que haya algun dato
	@Test
	void obtenerListaSueldosBase() {
		List<BigDecimal> sueldosBase = categoriaService.listarCategortias().stream().map(cat -> cat.getSueldoBase())
				.collect(Collectors.toList());
		assertTrue(sueldosBase.size() > 0);
	}

	// 4: Obtener la lista de Ids de categorias y que haya algun dato
	@Test
	void testObtenerListaId() {

		List<Integer> Ids = categoriaService.listarCategortias().stream().map(c -> c.getCategoriaId())
				.collect(Collectors.toList());

		assertTrue(Ids.size() > 0);

	}
	// 5: Obtener el nombre de categorias de aquellas que tengan sueldo base menor a
	// 7000

	@Test
	void listCategoriasSueldoMayor() {
		List<String> categorias = categoriaService.listarCategortias().stream()
				.filter(c -> c.getSueldoBase().compareTo(new BigDecimal(7000)) <= 0).map(c -> c.getNombre())
				.collect(Collectors.toList());

		assertTrue(categorias.size() > 0);
	}

	// 6: Verificar que todas las categorias tengna un sueldo base en positivo
	@Test
	void verificarCategoriasSueldoBPositivo() {
		List<Categoria> categorias = categoriaService.listarCategortias();
		List<Categoria> categoriasSueldoPos = categorias.stream()
				.filter(c -> c.getSueldoBase().compareTo(new BigDecimal(0)) == 1).collect(Collectors.toList());
		assertTrue(categoriasSueldoPos.size() == categorias.size());
	}
	// 7: Buscar alguna categoria cuyo que sea multiplo de 2000 y exista al menos
	// una

	@Test
	void testBuscarCategoriaMultiplo() {
		Optional<Categoria> opcategoria = categoriaService.listarCategortias().stream()
				.filter(c -> (c.getSueldoBase().doubleValue() % 2 == 0)).findAny();

		assertTrue(opcategoria.isPresent());
	}
	// 8: que haya al menos uno que empiece con V(mayuscula)

}
