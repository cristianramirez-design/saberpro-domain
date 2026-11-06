package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.List;
import java.util.Locale;

/**
 * VALUE OBJECT (inmutable). Justificacion pedagogica de la respuesta correcta.
 *
 * Invariantes:
 *  - no nula;
 *  - minimo 15 caracteres (despues de quitar espacios laterales);
 *  - no contiene palabras prohibidas.
 *
 * NOTA: la lista de palabras prohibidas no viene detallada en el enunciado del
 * taller; PALABRAS_PROHIBIDAS es un supuesto que debe confirmarse con la docente.
 */
public final class Justificacion {

    public static final int LONGITUD_MINIMA = 15;

    private static final List<String> PALABRAS_PROHIBIDAS = List.of("xxx", "asdf", "lorem ipsum");

    private final String texto;

    public Justificacion(String texto) {
        if (texto == null || texto.trim().length() < LONGITUD_MINIMA) {
            throw new ReglaNegocioException("La justificación debe contener al menos 15 caracteres.");
        }
        String limpio = texto.trim();
        String enMinusculas = limpio.toLowerCase(Locale.ROOT);
        for (String prohibida : PALABRAS_PROHIBIDAS) {
            if (enMinusculas.contains(prohibida)) {
                throw new ReglaNegocioException("La justificación contiene una palabra no permitida.");
            }
        }
        this.texto = limpio;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Justificacion)) return false;
        return texto.equals(((Justificacion) o).texto);
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
