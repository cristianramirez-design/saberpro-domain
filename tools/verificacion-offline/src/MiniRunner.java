import org.junit.jupiter.api.Test;
import java.lang.reflect.*;
import java.util.*;

/** Ejecuta las clases de prueba JUnit 5 por reflexion: una instancia nueva por
 *  metodo @Test, igual que hace JUnit. */
public class MiniRunner {
    public static void main(String[] args) throws Exception {
        int ok = 0, fail = 0;
        List<String> fallos = new ArrayList<>();
        for (String nombre : args) {
            Class<?> c = Class.forName(nombre);
            List<Method> tests = new ArrayList<>();
            for (Method m : c.getDeclaredMethods())
                if (m.isAnnotationPresent(Test.class)) tests.add(m);
            tests.sort(Comparator.comparing(Method::getName));
            System.out.println("\n--- " + c.getSimpleName() + " (" + tests.size() + " pruebas) ---");
            for (Method m : tests) {
                Constructor<?> ctor = c.getDeclaredConstructor();
                ctor.setAccessible(true);
                Object inst = ctor.newInstance();
                m.setAccessible(true);
                try {
                    m.invoke(inst);
                    ok++;
                    System.out.println("  PASS  " + m.getName());
                } catch (InvocationTargetException ite) {
                    fail++;
                    Throwable causa = ite.getCause();
                    System.out.println("  FAIL  " + m.getName() + " : " + causa);
                    fallos.add(c.getSimpleName() + "." + m.getName() + " -> " + causa);
                }
            }
        }
        System.out.println("\n=================================================");
        System.out.println(" Tests run: " + (ok + fail) + ", Failures: " + fail + ", Passed: " + ok);
        System.out.println("=================================================");
        for (String f : fallos) System.out.println("  " + f);
        if (fail > 0) System.exit(1);
    }
}
