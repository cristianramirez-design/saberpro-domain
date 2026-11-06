# Verificación sin acceso a Maven Central

> **La forma oficial de ejecutar las pruebas de este proyecto es `mvn test`, con
> JUnit 5.10.2 declarado en el `pom.xml`.** Lo que hay en esta carpeta es un
> recurso auxiliar para entornos sin acceso a Maven Central.

## Qué es

Una reimplementación mínima de la parte de la API de JUnit 5 que usan las pruebas
(`@Test` y los métodos de `Assertions`) más un ejecutor por reflexión
(`MiniRunner`). Esto permite **ejecutar las clases de prueba reales del proyecto**
—las mismas de `src/test/java`, sin modificarlas— cuando no se puede descargar la
librería.

No sustituye a JUnit: no tiene ciclo de vida (`@BeforeEach`, `@AfterEach`), ni
pruebas parametrizadas, ni informes. Solo crea una instancia nueva por método
`@Test` (igual que JUnit), lo invoca y reporta el resultado.

## Cómo ejecutarlo

Desde la raíz del repositorio:

```bash
./tools/verificacion-offline/ejecutar.sh
```

Salida esperada:

```
 Tests run: 38, Failures: 0, Passed: 38
```
