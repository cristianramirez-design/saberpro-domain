package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * ENTIDAD del Contexto de Revisión Pares.
 *
 * A diferencia de un Value Object, el Revisor tiene IDENTIDAD propia: el ID del
 * docente. Dos revisores son el mismo si comparten su identificador, aunque
 * cambien sus atributos (por ejemplo, si el docente cambia de nombre o se
 * desactiva). Por eso equals() y hashCode() se calculan SOLO sobre el id.
 *
 * Invariantes:
 *  - el id del docente es obligatorio;
 *  - el nombre no puede ser nulo ni vacío;
 *  - solo un revisor activo puede recibir asignaciones.
 */
public class Revisor {

    private final Long id;
    private String nombre;
    private boolean activo;

    public Revisor(Long id, String nombre) {
        this(id, nombre, true);
    }

    public Revisor(Long id, String nombre, boolean activo) {
        if (id == null) {
            throw new ReglaNegocioException("El revisor debe tener un identificador de docente.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException("El nombre del revisor no puede ser nulo ni vacío.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.activo = activo;
    }

    /** Comportamiento del dominio: un revisor inactivo no puede recibir asignaciones. */
    public boolean puedeRecibirAsignacion() {
        return this.activo;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new ReglaNegocioException("El nombre del revisor no puede ser nulo ni vacío.");
        }
        this.nombre = nuevoNombre.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    /** Igualdad por IDENTIDAD, no por atributos: así se comporta una Entidad. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revisor)) return false;
        return id.equals(((Revisor) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Revisor{id=" + id + ", nombre='" + nombre + "'}";
    }
}
