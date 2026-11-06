package co.edu.unicauca.saberpro.domain.repository;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.Optional;

/**
 * Puerto de persistencia del agregado Pregunta.
 * Es solo una interfaz pura: la implementación (JPA, Spring, etc.) vive fuera del dominio.
 */
public interface PreguntaRepository {

    Pregunta guardar(Pregunta pregunta);

    Optional<Pregunta> buscarPorId(Long id);
}
