package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RevisorTest {

    @Test
    void noDebePermitirRevisorSinIdentificador() {
        assertThrows(ReglaNegocioException.class, () -> new Revisor(null, "Paola Bedoya"));
    }

    @Test
    void noDebePermitirRevisorSinNombre() {
        assertThrows(ReglaNegocioException.class, () -> new Revisor(1L, "   "));
    }

    @Test
    void dosRevisoresConElMismoIdSonLaMismaEntidad() {
        Revisor a = new Revisor(100L, "Paola Bedoya");
        Revisor b = new Revisor(100L, "P. A. Bedoya Toro", false);
        assertEquals(a, b, "la identidad de una entidad no depende de sus atributos");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void dosRevisoresConIdDistintoSonEntidadesDistintas() {
        assertNotEquals(new Revisor(100L, "Mismo Nombre"), new Revisor(200L, "Mismo Nombre"));
    }

    @Test
    void revisorInactivoNoPuedeRecibirAsignacion() {
        Revisor revisor = new Revisor(1L, "Docente", true);
        assertTrue(revisor.puedeRecibirAsignacion());
        revisor.desactivar();
        assertFalse(revisor.puedeRecibirAsignacion());
    }

    @Test
    void cambiarNombreNoAlteraLaIdentidad() {
        Revisor revisor = new Revisor(7L, "Nombre Inicial");
        revisor.cambiarNombre("Nombre Corregido");
        assertEquals("Nombre Corregido", revisor.getNombre());
        assertEquals(new Revisor(7L, "Cualquiera"), revisor);
    }
}
