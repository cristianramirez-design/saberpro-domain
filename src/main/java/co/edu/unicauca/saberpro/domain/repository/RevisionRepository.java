package co.edu.unicauca.saberpro.domain.repository;

import co.edu.unicauca.saberpro.domain.model.Revision;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia del agregado Revision (Contexto de Revisión Pares).
 * Interfaz pura: la implementación técnica vive fuera del dominio.
 */
public interface RevisionRepository {

    Revision guardar(Revision revision);

    Optional<Revision> buscarPorId(Long id);

    List<Revision> buscarPorPregunta(Long preguntaId);
}
