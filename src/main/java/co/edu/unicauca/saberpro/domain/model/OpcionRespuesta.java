package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/**
 * VALUE OBJECT (inmutable). Una opcion de respuesta: texto + indicador de clave.
 * Invariante: el texto no puede ser nulo ni estar vacio.
 */
public final class OpcionRespuesta {

    private final String texto;
    private final boolean esCorrecta;

    public OpcionRespuesta(String texto, boolean esCorrecta) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El texto de la opción no puede ser nulo ni estar vacío.");
        }
        this.texto = texto.trim();
        this.esCorrecta = esCorrecta;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpcionRespuesta)) return false;
        OpcionRespuesta that = (OpcionRespuesta) o;
        return esCorrecta == that.esCorrecta && texto.equals(that.texto);
    }

    @Override
    public int hashCode() {
        return 31 * texto.hashCode() + (esCorrecta ? 1 : 0);
    }

    @Override
    public String toString() {
        return texto + (esCorrecta ? " [clave]" : "");
    }
}
