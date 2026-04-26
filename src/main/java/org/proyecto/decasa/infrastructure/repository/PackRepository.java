package org.proyecto.decasa.infrastructure.repository;

import org.proyecto.decasa.domain.entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PackRepository extends JpaRepository<Pack, Long> {

    // Carga el pack junto con sus componentes en una sola query.
    // Sin JOIN FETCH, acceder a pack.getComponents() dispararía una query
    // extra por cada pack — eso se llama el problema N+1.
    @Query("""
            SELECT DISTINCT p FROM Pack p
            LEFT JOIN FETCH p.components c
            LEFT JOIN FETCH c.product
            WHERE p.available = true
            ORDER BY p.sortOrder ASC
            """)
    List<Pack> findAvailableWithComponents();

    @Query("""
            SELECT p FROM Pack p
            LEFT JOIN FETCH p.components c
            LEFT JOIN FETCH c.product
            WHERE p.id = :id
            """)
    Optional<Pack> findByIdWithComponents(Long id);
}
