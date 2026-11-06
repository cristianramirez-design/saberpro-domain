#!/usr/bin/env bash
# Ejecuta las clases de prueba JUnit 5 del proyecto sin descargar JUnit.
# La via oficial sigue siendo "mvn test"; esto es solo para entornos sin red.
set -euo pipefail

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
OUT="${RAIZ}/target/verificacion-offline"
SRC="${RAIZ}/tools/verificacion-offline/src"

rm -rf "${OUT}"
mkdir -p "${OUT}/dominio" "${OUT}/junit" "${OUT}/pruebas"

echo "==> Compilando el dominio"
javac -Xlint:all -encoding UTF-8 -d "${OUT}/dominio" \
      $(find "${RAIZ}/src/main" -name "*.java")

echo "==> Compilando la API minima de JUnit y el ejecutor"
javac -encoding UTF-8 -d "${OUT}/junit" $(find "${SRC}" -name "*.java")

echo "==> Compilando las clases de prueba del proyecto"
javac -Xlint:all -encoding UTF-8 -cp "${OUT}/dominio:${OUT}/junit" \
      -d "${OUT}/pruebas" $(find "${RAIZ}/src/test" -name "*.java")

echo "==> Ejecutando"
java -Dfile.encoding=UTF-8 -cp "${OUT}/dominio:${OUT}/junit:${OUT}/pruebas" MiniRunner \
  co.edu.unicauca.saberpro.domain.model.PreguntaTest \
  co.edu.unicauca.saberpro.domain.model.ValueObjectsTest \
  co.edu.unicauca.saberpro.domain.model.RevisorTest \
  co.edu.unicauca.saberpro.domain.model.RevisionTest
