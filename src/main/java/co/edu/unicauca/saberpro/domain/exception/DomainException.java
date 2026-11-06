package co.edu.unicauca.saberpro.domain.exception;

/**
 * Excepcion base de la capa de dominio. No depende de ningun framework.
 */
public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DomainException(String mensaje) {
        super(mensaje);
    }
}
