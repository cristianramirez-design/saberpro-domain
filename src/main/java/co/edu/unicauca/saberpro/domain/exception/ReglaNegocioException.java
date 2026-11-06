package co.edu.unicauca.saberpro.domain.exception;

/**
 * Se lanza cuando se viola una invariante o regla de negocio del dominio.
 */
public class ReglaNegocioException extends DomainException {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
