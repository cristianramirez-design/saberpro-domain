package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @Test
    void justificacionNulaLanzaExcepcion() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(null));
    }

    @Test
    void justificacionCortaLanzaExcepcion() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion("muy corta"));
    }

    @Test
    void justificacionSeRecortaAlCrearla() {
        Justificacion j = new Justificacion("   texto suficientemente largo   ");
        assertEquals("texto suficientemente largo", j.getTexto());
    }

    @Test
    void justificacionConPalabraProhibidaLanzaExcepcion() {
        assertThrows(ReglaNegocioException.class,
                () -> new Justificacion("Esta justificación contiene xxx y es válida"));
    }

    @Test
    void enunciadoNuloOVacioLanzaExcepcion() {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(null));
        assertThrows(ReglaNegocioException.class, () -> new Enunciado("   "));
    }

    @Test
    void opcionRespuestaSinTextoLanzaExcepcion() {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta("", true));
    }

    @Test
    void valueObjectsConMismosAtributosSonIguales() {
        assertEquals(new Enunciado("Mismo texto"), new Enunciado("Mismo texto"));
        assertEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", true));
        assertNotEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", false));
    }
}
