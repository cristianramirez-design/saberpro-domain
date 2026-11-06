package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ENTIDAD y raíz del agregado del Contexto de Revisión Pares.
 *
 * Su identidad es el ID de la evaluación. Modela el ciclo completo del Lenguaje
 * Ubicuo de ese contexto: la Asignacion de un Revisor a una pregunta y la
 * Evaluacion que termina con un Dictamen.
 *
 * Referencia a los otros dos agregados (Pregunta y Revisor) SOLO por su
 * identificador, nunca por objeto: así no se acoplan dos agregados distintos ni
 * se permite mutar uno desde fuera de su propia raíz.
 *
 * Invariantes:
 *  - R1: id de evaluación, revisor, pregunta y autor de la pregunta son obligatorios;
 *  - R2: solo se asigna a un revisor activo (precondición de la asignación);
 *  - R3: un revisor no puede evaluar una pregunta de su propia autoría;
 *  - R4: el dictamen se emite una sola vez;
 *  - R5: un dictamen distinto de APROBADA exige observaciones.
 */
public class Revision {

    private final Long id;
    private final Long revisorId;
    private final Long preguntaId;
    private final LocalDateTime fechaAsignacion;

    private Dictamen dictamen;
    private String observacion;
    private LocalDateTime fechaDictamen;

    private Revision(Long id, Revisor revisor, Long preguntaId, Long autorPreguntaId) {
        if (id == null) {
            throw new ReglaNegocioException("La revisión debe tener un identificador de evaluación.");
        }
        if (revisor == null) {
            throw new ReglaNegocioException("La revisión debe tener un revisor asignado.");
        }
        if (preguntaId == null) {
            throw new ReglaNegocioException("La revisión debe referenciar una pregunta.");
        }
        if (autorPreguntaId == null) {
            throw new ReglaNegocioException(
                    "Debe conocerse el autor de la pregunta para poder asignar la revisión.");
        }
        if (!revisor.puedeRecibirAsignacion()) {
            throw new ReglaNegocioException("No se puede asignar una revisión a un revisor inactivo.");
        }
        if (autorPreguntaId.equals(revisor.getId())) {
            throw new ReglaNegocioException("Un revisor no puede evaluar una pregunta de su propia autoría.");
        }
        this.id = id;
        this.revisorId = revisor.getId();
        this.preguntaId = preguntaId;
        this.fechaAsignacion = LocalDateTime.now();
    }

    /**
     * ASIGNACION: crea la revisión que vincula a un revisor con una pregunta.
     *
     * Se recibe el Revisor completo para poder comprobar que está activo (R2) y que
     * no es el autor (R3), pero la revisión solo conserva su identificador. No hay
     * sobrecargas: los cuatro datos son obligatorios, de modo que ninguna invariante
     * puede desactivarse desde el exterior.
     *
     * @param autorPreguntaId identificador del autor de la pregunta, obligatorio.
     */
    public static Revision asignar(Long id, Revisor revisor, Long preguntaId, Long autorPreguntaId) {
        return new Revision(id, revisor, preguntaId, autorPreguntaId);
    }

    /** EVALUACION: el revisor emite su dictamen. Solo puede hacerlo una vez. */
    public void emitirDictamen(Dictamen nuevoDictamen, String observacion) {
        if (this.dictamen != null) {
            throw new ReglaNegocioException("El dictamen de esta revisión ya fue emitido.");
        }
        if (nuevoDictamen == null) {
            throw new ReglaNegocioException("Debe indicarse un dictamen.");
        }
        if (nuevoDictamen.exigeObservacion() && (observacion == null || observacion.isBlank())) {
            throw new ReglaNegocioException(
                    "Un dictamen distinto de APROBADA exige observaciones para el autor.");
        }
        this.dictamen = nuevoDictamen;
        this.observacion = observacion == null ? null : observacion.trim();
        this.fechaDictamen = LocalDateTime.now();
    }

    public boolean estaPendiente() {
        return this.dictamen == null;
    }

    public Long getId() {
        return id;
    }

    public Long getRevisorId() {
        return revisorId;
    }

    public Long getPreguntaId() {
        return preguntaId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public Dictamen getDictamen() {
        return dictamen;
    }

    public String getObservacion() {
        return observacion;
    }

    public LocalDateTime getFechaDictamen() {
        return fechaDictamen;
    }

    /** Igualdad por IDENTIDAD (ID de evaluación). */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revision)) return false;
        return id.equals(((Revision) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Revision{id=" + id + ", preguntaId=" + preguntaId
                + ", dictamen=" + (dictamen == null ? "PENDIENTE" : dictamen) + "}";
    }
}
