package org.proyecto.decasa.infrastructure.repository;

import org.proyecto.decasa.domain.entity.Category;
import org.proyecto.decasa.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de categorías.
 *
 * JpaRepository<Category, Long> nos da gratis:
 *   findAll(), findById(), save(), delete(), count()...
 *
 * Spring Data genera la implementación SQL en tiempo de arranque
 * leyendo el nombre del método — no hay que escribir SQL.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Spring Data traduce esto a: SELECT * FROM category WHERE name = ?
    Optional<Category> findByName(CategoryType name);

    // Devuelve todas las categorías ordenadas por sort_order ASC
    java.util.List<Category> findAllByOrderBySortOrderAsc();
}
