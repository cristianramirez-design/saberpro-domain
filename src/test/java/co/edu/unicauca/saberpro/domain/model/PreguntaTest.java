package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreguntaTest {

    private Pregunta preguntaConCuatroOpciones() {
        Pregunta p = new Pregunta(
                new Enunciado("¿Cuál es el patrón que desacopla la creación de objetos?"),
                new Justificacion("La fábrica encapsula la creación y desacopla al cliente."));
        p.agregarOpcion("Fábrica", true);
        p.agregarOpcion("Singleton", false);
        p.agregarOpcion("Observador", false);
        p.agregarOpcion("Adaptador", false);
        return p;
    }

    @Test
    void noDebePermitirEnviarARevisionSinCuatroOpciones() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("Opción A", true);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void noDebePermitirMasDeCuatroOpciones() {
        Pregunta pregunta = preguntaConCuatroOpciones();
        assertThrows(ReglaNegocioException.class, () -> pregunta.agregarOpcion("Quinta", false));
        assertEquals(4, pregunta.getOpciones().size());
    }

    @Test
    void noDebePermitirEnviarConDosClaves() {
        Pregunta pregunta = new Pregunta(new Enunciado("Enunciado válido"),
                new Justificacion("Justificación suficientemente larga"));
        pregunta.agregarOpcion("A", true);
        pregunta.agregarOpcion("B", true);
        pregunta.agregarOpcion("C", false);
        pregunta.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }

    @Test
    void noDebePermitirEnviarSinClave() {
        Pregunta pregunta = new Pregunta(new Enunciado("Enunciado válido"),
                new Justificacion("Justificación suficientemente larga"));
        for (String t : new String[]{"A", "B", "C", "D"}) {
            pregunta.agregarOpcion(t, false);
        }
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarSinEnunciadoNiJustificacion() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("A", true);
        pregunta.agregarOpcion("B", false);
        pregunta.agregarOpcion("C", false);
        pregunta.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void enviarARevisionValidaCambiaElEstado() {
        Pregunta pregunta = preguntaConCuatroOpciones();
        pregunta.enviarARevision();
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
    }

    @Test
    void noDebePermitirModificarOpcionesFueraDeBorrador() {
        Pregunta pregunta = preguntaConCuatroOpciones();
        pregunta.enviarARevision();
        assertThrows(ReglaNegocioException.class, () -> pregunta.agregarOpcion("Otra", false));
    }

    @Test
    void noDebePermitirEnviarDosVeces() {
        Pregunta pregunta = preguntaConCuatroOpciones();
        pregunta.enviarARevision();
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void dosPreguntasConElMismoIdSonLaMismaEntidad() {
        Pregunta a = new Pregunta(42L, new Enunciado("Un enunciado"),
                new Justificacion("Justificación suficientemente larga"));
        Pregunta b = new Pregunta(42L, new Enunciado("Otro enunciado distinto"),
                new Justificacion("Otra justificación igualmente larga"));
        assertEquals(a, b, "la identidad de una entidad no depende de sus atributos");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void unaPreguntaSinIdentidadSoloEsIgualASiMisma() {
        Pregunta a = new Pregunta();
        Pregunta b = new Pregunta();
        assertEquals(a, a);
        assertNotEquals(a, b);
    }

    @Test
    void laIdentidadSeAsignaUnaSolaVez() {
        Pregunta pregunta = new Pregunta();
        pregunta.asignarId(7L);
        assertEquals(7L, pregunta.getId());
        assertThrows(ReglaNegocioException.class, () -> pregunta.asignarId(8L));
    }

    @Test
    void lasOpcionesExpuestasNoSonModificables() {
        Pregunta pregunta = preguntaConCuatroOpciones();
        assertThrows(UnsupportedOperationException.class,
                () -> pregunta.getOpciones().add(new OpcionRespuesta("X", false)));
    }
}
