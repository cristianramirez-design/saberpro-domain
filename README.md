# Banco de Preguntas Saber PRO — Módulo de Dominio (DDD)

Taller 11 de **Laboratorio de Ingeniería de Software II** — Universidad del Cauca, 2026.2
Docente: Ing. Paola Andrea Bedoya Toro

Módulo de dominio del Banco de Preguntas Saber PRO implementado con Domain-Driven Design
en **Java 17 puro**: sin Spring, sin JPA y sin ninguna otra dependencia de framework.

## Grupo 4

| | Integrante |
|---|---|
| 1 | Santiago Martinez |
| 2 | Andres Felipe Florin |
| 3 | Cristian Camilo Ramirez Camacho |

Grupo de 3 integrantes, autorizado por la docente.

## Estructura

```
saberpro-domain/
├── pom.xml                        # Java 17 · JUnit 5.10.2 (scope test)
├── informe/
│   └── lisw2_taller11_g4.docx     # Informe técnico
├── src/main/java/co/edu/unicauca/saberpro/domain/
│   ├── model/
│   │   ├── Pregunta.java          # AGGREGATE ROOT
│   │   ├── Enunciado.java         # VALUE OBJECT (inmutable)
│   │   ├── Justificacion.java     # VALUE OBJECT (inmutable)
│   │   ├── OpcionRespuesta.java   # VALUE OBJECT (inmutable)
│   │   ├── EstadoPregunta.java    # ENUM
│   │   ├── Revisor.java           # ENTITY  (identidad: ID de docente)
│   │   ├── Revision.java          # ENTITY / AGGREGATE ROOT (ID de evaluación)
│   │   └── Dictamen.java          # ENUM
│   ├── exception/
│   │   ├── DomainException.java
│   │   └── ReglaNegocioException.java
│   └── repository/
│       ├── PreguntaRepository.java   # puerto puro
│       └── RevisionRepository.java   # puerto puro
├── src/test/java/...              # 38 pruebas JUnit 5
└── tools/
    └── verificacion-offline/      # ejecutar las pruebas sin descargar JUnit
```

## Bounded Contexts

| Contexto | Responsabilidad | Lenguaje Ubicuo |
|---|---|---|
| Autoría | Gestión de reactivos DCE, enunciados, distractores y justificaciones | Pregunta, Enunciado, Clave, Justificacion, Autor |
| Validación | Verificación de reglas de calidad estructural y pedagógica | ReglaEstructural, FiltroExpresion, ValidezOpciones |
| Ciclo de Vida | Control estricto de estados y transiciones | EstadoPregunta, Transicion, Borrador, EnRevision |
| Revisión Pares | Asignación de evaluadores, dictamen y observaciones | Revisor, Asignacion, Evaluacion, Dictamen |

## Invariantes de negocio

**Agregado `Pregunta`**

- **I1** — Solo se pueden modificar opciones en estado `BORRADOR`.
- **I2** — Una pregunta no puede tener más de 4 opciones.
- **I3** — Para enviarse a revisión debe tener enunciado, justificación, exactamente 4 opciones y exactamente 1 clave correcta.
- **I4** — Solo una pregunta en `BORRADOR` puede enviarse a revisión.
- **I5** — La identidad se asigna una sola vez; sin identidad, una pregunta solo es igual a sí misma.

**Agregado `Revision`**

- **R1** — Id de evaluación, revisor, pregunta y autor de la pregunta son obligatorios.
- **R2** — Solo se asigna una revisión a un revisor activo.
- **R3** — Un revisor no puede evaluar una pregunta de su propia autoría.
- **R4** — El dictamen se emite una sola vez.
- **R5** — Un dictamen distinto de `APROBADA` exige observaciones.

## Compilar y probar

```bash
mvn test       # ejecuta las 38 pruebas JUnit 5
mvn package    # empaqueta el módulo
```

### Verificación sin acceso a Maven Central

Para entornos sin red, `tools/verificacion-offline/` permite ejecutar **las mismas
clases de prueba**, sin modificarlas, con una reimplementación mínima de `@Test` y
`Assertions` y un ejecutor por reflexión:

```bash
./tools/verificacion-offline/ejecutar.sh
```

Salida esperada: `Tests run: 38, Failures: 0, Passed: 38`.

Es solo un recurso auxiliar; la vía oficial sigue siendo `mvn test`.

## Aislamiento del dominio

El núcleo no depende de ningún framework. Comprobación:

```bash
grep -rn -E "springframework|jakarta.persistence|hibernate|lombok" src/main
# (sin resultados)
```

Todos los `import` de `src/main` son del propio dominio o del JDK estándar
(`java.util`, `java.time`). JUnit 5 es la única dependencia del `pom.xml` y está
limitada al ámbito `test`.
