package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * AGGREGATE ROOT. Pregunta de selección múltiple del Banco de Preguntas Saber PRO.
 *
 * Invariantes garantizadas por el agregado:
 *  - solo se pueden modificar opciones en estado BORRADOR;
 *  - no puede tener más de 4 opciones;
 *  - para ser enviada a revisión debe tener enunciado, justificación,
 *    exactamente 4 opciones y exactamente 1 clave correcta.
 */
public class Pregunta {

    public static final int NUMERO_OPCIONES = 4;

    private Long id;
    private Enunciado enunciado;
    private Justificacion justificacion;
    private final List<OpcionRespuesta> opciones = new ArrayList<>();
    private EstadoPregunta estado = EstadoPregunta.BORRADOR;

    /** Crea una pregunta vacía en estado BORRADOR. */
    public Pregunta() {
    }

    public Pregunta(Enunciado enunciado, Justificacion justificacion) {
        this.enunciado = enunciado;
        this.justificacion = justificacion;
    }

    /** Crea una pregunta que ya tiene identidad asignada (por ejemplo, al recuperarla del repositorio). */
    public Pregunta(Long id, Enunciado enunciado, Justificacion justificacion) {
        this(enunciado, justificacion);
        this.id = id;
    }

    /**
     * Asigna la identidad una única vez. El repositorio la invoca cuando la pregunta
     * se persiste por primera vez; después, la identidad es inmutable.
     */
    public void asignarId(Long nuevoId) {
        if (nuevoId == null) {
            throw new ReglaNegocioException("El identificador de la pregunta no puede ser nulo.");
        }
        if (this.id != null) {
            throw new ReglaNegocioException("La pregunta ya tiene una identidad asignada.");
        }
        this.id = nuevoId;
    }

    public void agregarOpcion(String texto, boolean esCorrecta) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se pueden modificar opciones en estado Borrador.");
        }
        if (this.opciones.size() >= NUMERO_OPCIONES) {
            throw new ReglaNegocioException("Una pregunta no puede tener más de 4 opciones.");
        }
        this.opciones.add(new OpcionRespuesta(texto, esCorrecta));
    }

    public void enviarARevision() {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo una pregunta en Borrador puede enviarse a revisión.");
        }
        validarInvariantesParaEnvio();
        this.estado = EstadoPregunta.PENDIENTE_REVISION;
    }

    private void validarInvariantesParaEnvio() {
        if (this.enunciado == null) {
            throw new ReglaNegocioException("La pregunta debe tener un enunciado.");
        }
        if (this.justificacion == null) {
            throw new ReglaNegocioException("La pregunta debe tener una justificación.");
        }
        if (this.opciones.size() != NUMERO_OPCIONES) {
            throw new ReglaNegocioException("Debe tener exactamente 4 opciones de respuesta.");
        }
        long correctas = this.opciones.stream().filter(OpcionRespuesta::isEsCorrecta).count();
        if (correctas != 1) {
            throw new ReglaNegocioException("Debe existir exactamente 1 opción correcta.");
        }
    }

    public Long getId() {
        return id;
    }

    public Enunciado getEnunciado() {
        return enunciado;
    }

    public Justificacion getJustificacion() {
        return justificacion;
    }

    /** Vista de solo lectura: el agregado controla sus propias modificaciones. */
    public List<OpcionRespuesta> getOpciones() {
        return Collections.unmodifiableList(opciones);
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    /**
     * Igualdad por IDENTIDAD, como corresponde a una entidad. Una pregunta que aún
     * no ha sido persistida no tiene identidad, así que solo es igual a sí misma.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pregunta)) return false;
        Pregunta otra = (Pregunta) o;
        return this.id != null && this.id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : Objects.hash(id);
    }
}
