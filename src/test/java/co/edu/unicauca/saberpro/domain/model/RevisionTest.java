package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RevisionTest {

    private static final Long ID_PREGUNTA = 5L;
    private static final Long ID_AUTOR = 77L;

    private final Revisor revisor = new Revisor(10L, "Paola Bedoya");

    private Revision revisionPendiente() {
        return Revision.asignar(1L, revisor, ID_PREGUNTA, ID_AUTOR);
    }

    @Test
    void noDebePermitirAsignarSinIdentificadorDeEvaluacion() {
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(null, revisor, ID_PREGUNTA, ID_AUTOR));
    }

    @Test
    void noDebePermitirAsignarSinRevisor() {
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(1L, null, ID_PREGUNTA, ID_AUTOR));
    }

    @Test
    void noDebePermitirAsignarSinPregunta() {
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(1L, revisor, null, ID_AUTOR));
    }

    @Test
    void noDebePermitirAsignarSinConocerElAutor() {
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(1L, revisor, ID_PREGUNTA, null));
    }

    @Test
    void noDebePermitirAsignarARevisorInactivo() {
        Revisor inactivo = new Revisor(20L, "Docente", false);
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(1L, inactivo, ID_PREGUNTA, ID_AUTOR));
    }

    @Test
    void noDebePermitirQueElAutorReviseSuPropiaPregunta() {
        assertThrows(ReglaNegocioException.class,
                () -> Revision.asignar(1L, revisor, ID_PREGUNTA, revisor.getId()));
    }

    @Test
    void laRevisionSoloGuardaElIdentificadorDelRevisor() {
        Revision revision = revisionPendiente();
        assertEquals(revisor.getId(), revision.getRevisorId());
        assertEquals(ID_PREGUNTA, revision.getPreguntaId());
    }

    @Test
    void laRevisionNaceEnEstadoPendiente() {
        Revision revision = revisionPendiente();
        assertTrue(revision.estaPendiente());
        assertNull(revision.getDictamen());
        assertNotNull(revision.getFechaAsignacion());
    }

    @Test
    void dictamenAprobadaNoExigeObservacion() {
        Revision revision = revisionPendiente();
        revision.emitirDictamen(Dictamen.APROBADA, null);
        assertEquals(Dictamen.APROBADA, revision.getDictamen());
        assertFalse(revision.estaPendiente());
        assertNotNull(revision.getFechaDictamen());
    }

    @Test
    void dictamenDevueltaExigeObservacion() {
        Revision revision = revisionPendiente();
        assertThrows(ReglaNegocioException.class,
                () -> revision.emitirDictamen(Dictamen.DEVUELTA_CON_OBSERVACIONES, "  "));
        assertTrue(revision.estaPendiente());
    }

    @Test
    void dictamenRechazadaExigeObservacion() {
        Revision revision = revisionPendiente();
        assertThrows(ReglaNegocioException.class,
                () -> revision.emitirDictamen(Dictamen.RECHAZADA, null));
    }

    @Test
    void noDebePermitirEmitirElDictamenDosVeces() {
        Revision revision = revisionPendiente();
        revision.emitirDictamen(Dictamen.APROBADA, null);
        assertThrows(ReglaNegocioException.class,
                () -> revision.emitirDictamen(Dictamen.RECHAZADA, "Ya no aplica"));
    }

    @Test
    void dosRevisionesConElMismoIdSonLaMismaEntidad() {
        Revision a = Revision.asignar(99L, revisor, ID_PREGUNTA, ID_AUTOR);
        Revision b = Revision.asignar(99L, new Revisor(30L, "Otro"), 8L, ID_AUTOR);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
