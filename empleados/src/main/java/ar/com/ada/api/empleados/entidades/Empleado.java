package ar.com.ada.api.empleados.entidades;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "empleado")

@NamedQuery(name = "Empleado.findAllByCategoriaId", query = "SELECT h FROM Empleado h WHERE id_categoria = ?1")
public class Empleado {
    @Id
    @Column(name = "id_empleado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empleadoId;
    private String nombre;
    private int edad;
    private int dni;
    private BigDecimal sueldo;
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    @Column(name = "fecha_baja")
    private Date fechaBaja;
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;
    @Column(name = "id_estado")
    private int estadoId;

    /**
     * @return the empleadoId
     */
    public int getEmpleadoId() {
        return empleadoId;
    }

    /**
     * @param empleadoId the empleadoId to set
     */
    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
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
    }

    /**
     * @return the edad
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @param edad the edad to set
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * @return the sueldo
     */
    public BigDecimal getSueldo() {
        return sueldo;
    }

    /**
     * @param sueldo the sueldo to set
     */
    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    /**
     * @return the fechaAlta
     */
    public Date getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * @return the fechaBaja
     */
    public Date getFechaBaja() {
        return fechaBaja;
    }

    /**
     * @param fechaBaja the fechaBaja to set
     */
    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @JsonIgnore
    public BigDecimal getVentasActuales() {

        Random randomGenerator = new Random();

        // Genero un numero rando hasta 10000
        double venta = randomGenerator.nextDouble() * 10000 + 1;
        // redondeo en 2 decimales el random truncando
        venta = ((long) (venta * 100)) / 100d;

        return new BigDecimal(venta);
    }

    public EmpleadoEstadoEnum getEstadoId() {
        return EmpleadoEstadoEnum.parse(this.estadoId);
    }

    public void setEstadoId(EmpleadoEstadoEnum estadoId) {
        this.estadoId = estadoId.getValue();
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        // por el ManyToOne
        // a la lista de empleados va a agregarle el obj
        // devuelve la lista de empleados de la categoria actual y me agrega a m� mismo
        // (o sea, a categoria)
        this.categoria.getEmpleados().add(this);
    }

    /***
     * En este caso es un ENUMERADO con numeracion customizada En JAVA, los
     * enumerados con numeros customizados deben tener un constructor y un
     * comparador para poder funcionar correctamente
     */
    public enum EmpleadoEstadoEnum {
        DESCONOCIDO(0), PENDIENTEALTA(1), ACTIVO(2), LICENCIA(3), DESVINCULADO(99);

        private final int value;

        // NOTE: Enum constructor tiene que estar en privado
        private EmpleadoEstadoEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EmpleadoEstadoEnum parse(int id) {
            EmpleadoEstadoEnum status = null; // Default
            for (EmpleadoEstadoEnum item : EmpleadoEstadoEnum.values()) {
                if (item.getValue() == id) {
                    status = item;
                    break;
                }
            }
            return status;
        }
    }
}
