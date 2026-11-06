package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/**
 * VALUE OBJECT (inmutable). Texto del enunciado de una pregunta.
 * Invariante: no puede ser nulo ni estar vacio.
 */
public final class Enunciado {

    private final String texto;

    public Enunciado(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El enunciado no puede ser nulo ni estar vacio.");
        }
        this.texto = texto.trim();
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enunciado)) return false;
        return texto.equals(((Enunciado) o).texto);
    }

    @Override
    public int hashCode() {
        return texto.hashCode();
    }

    @Override
    public String toString() {
        return texto;
    }
}
