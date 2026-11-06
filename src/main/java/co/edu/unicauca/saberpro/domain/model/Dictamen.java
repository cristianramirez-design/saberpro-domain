package co.edu.unicauca.saberpro.domain.model;

/**
 * Dictamen emitido por un revisor sobre una pregunta.
 * Concepto del Lenguaje Ubicuo del Contexto de Revisión Pares.
 */
public enum Dictamen {

    /** La pregunta cumple los criterios y queda aprobada. */
    APROBADA,

    /** La pregunta debe corregirse; regresa al autor con observaciones. */
    DEVUELTA_CON_OBSERVACIONES,

    /** La pregunta se descarta del banco. */
    RECHAZADA;

    /** Un dictamen distinto de APROBADA exige observaciones que guíen al autor. */
    public boolean exigeObservacion() {
        return this != APROBADA;
    }
}
