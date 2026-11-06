package org.junit.jupiter.api;
import org.junit.jupiter.api.function.Executable;
import java.util.Objects;

/** Implementacion minima pero REAL de la API de asercion de JUnit 5,
 *  para ejecutar las pruebas en entornos sin acceso a Maven Central. */
public class Assertions {

    private static AssertionError fallo(String m, String detalle) {
        return new AssertionError((m == null ? "" : m + " ==> ") + detalle);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T assertThrows(Class<T> esperada, Executable e) {
        return assertThrows(esperada, e, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T assertThrows(Class<T> esperada, Executable e, String m) {
        try {
            e.execute();
        } catch (Throwable t) {
            if (esperada.isInstance(t)) return (T) t;
            throw fallo(m, "se esperaba " + esperada.getName() + " pero se lanzo " + t.getClass().getName());
        }
        throw fallo(m, "se esperaba que se lanzara " + esperada.getName() + " pero no se lanzo nada");
    }

    public static void assertEquals(Object a, Object b) { assertEquals(a, b, null); }

    public static void assertEquals(Object a, Object b, String m) {
        if (!Objects.equals(a, b)) throw fallo(m, "esperado <" + a + "> pero fue <" + b + ">");
    }

    public static void assertEquals(int a, int b) { assertEquals(a, b, null); }

    public static void assertEquals(int a, int b, String m) {
        if (a != b) throw fallo(m, "esperado <" + a + "> pero fue <" + b + ">");
    }

    public static void assertNotEquals(Object a, Object b) {
        if (Objects.equals(a, b)) throw fallo(null, "no se esperaba <" + a + ">");
    }

    public static void assertTrue(boolean c) { assertTrue(c, null); }
    public static void assertTrue(boolean c, String m) { if (!c) throw fallo(m, "esperado true"); }
    public static void assertFalse(boolean c) { if (c) throw fallo(null, "esperado false"); }
    public static void assertNull(Object o) { if (o != null) throw fallo(null, "esperado null, fue <" + o + ">"); }
    public static void assertNotNull(Object o) { if (o == null) throw fallo(null, "esperado no-null"); }
}
