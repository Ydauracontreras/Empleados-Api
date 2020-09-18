package ar.com.ada.api.empleados.entidades;

import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.com.ada.api.empleados.entidades.calculos.SueldoAdministrativoCalculartor;
import ar.com.ada.api.empleados.entidades.calculos.SueldoAuxiliarCalculator;
import ar.com.ada.api.empleados.entidades.calculos.SueldoCalculator;
import ar.com.ada.api.empleados.entidades.calculos.SueldoVentasCalculator;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoriaId;
    private String nombre;
    @Column(name = "sueldo_base")
    private BigDecimal sueldoBase;
    // OneToMany la clase categoria va de una a muchas en empleado, va a aparecer
    // varias veces una misma categoria
    // mappedBy como voy a referenciarme al obj categoria pero desde el punto de
    // vista empleado, "categoria"
    // indica que le va a dar bola al atributo categoria que tenga el obj empleado,
    // nombre del atributo en el obj (atributo categoria
    // en el obj empleado), se tiene que llamar "categoria" el atributo en la clase
    // Empleado (sino no lo va a encontrar)
    // cascade, si traemos desde el repo un obj categoria el cascadeType.All va a
    // traer todos los empleados
    // fetch, obliga que traiga a todos de una, no se suele utilizar, es por fines
    // educativos, se suele usar LAZY
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // Ignora ese atributo cuando front lo manda
    @JsonIgnore
    private List<Empleado> empleados = new ArrayList<>();

    /**
     * - Pattern Desgign Strategy: en este caso, se uso como modelo el calculo de
     * sueldos de un empleado. Si el empleado es Administrativo, el sueldo actual no
     * puede ser menor al sueldo de la categoria cuando haya recalculo de sueldos.
     * En el caso de un Auxiliar, el sueldo actual siempre es el sueldo de la
     * categoria . En caso de vendedores, se usa el sueldo de la categoria base +
     * 10% de comisiones sobre ventas. Para este caso se puso una interface
     * SueldoCalculator, que se usa en la clase Categoria para calcular el sueldo.
     * Cuando una categoria tenga nombre Administrativo, Pasasante
     */
    @JsonIgnore
    @Transient // No meterlo a la base de datos. Es transitorio
    private SueldoCalculator sueldoStrategy;

    public int getCategoriaId() {
        return categoriaId;
    }

    /**
     * @param categoriaId the categoriaId to set
     */
    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
        switch (this.nombre) {

            case "Ventas":
                this.setSueldoStrategy(new SueldoVentasCalculator());
                break;
            case "Administrativa":
            case "Administrativo":
                this.setSueldoStrategy(new SueldoAdministrativoCalculartor());
                break;
            case "Auxiliar":
                this.setSueldoStrategy(new SueldoAuxiliarCalculator());
                break;

            default:

                // Por ahor default lo ponemos como Administrativo
                this.setSueldoStrategy(new SueldoAdministrativoCalculartor());
                break;
        }
    }

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    /**
     * @param sueldoBase the sueldoBase to set
     */
    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    /**
     * @return the empleados
     */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /**
     * @param empleados the empleados to set
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    /**
     * @return the sueldoStrategy
     */
    public SueldoCalculator getSueldoStrategy() {

        // si esta en nulo, le genero la estrategy a traves del nombre
        // Cuando vienen desde la db este valor esta en nulo
        // por lo cual lo reasigno.
        if (this.sueldoStrategy == null)
            this.setNombre(this.getNombre());
        return sueldoStrategy;
    }

    /**
     * @param sueldoStrategy the sueldoStrategy to set
     */
    public void setSueldoStrategy(SueldoCalculator sueldoStrategy) {
        this.sueldoStrategy = sueldoStrategy;
    }

    public BigDecimal calcularSueldo(Empleado empleado) {
        return this.getSueldoStrategy().calcularSueldo(empleado);
    }
}