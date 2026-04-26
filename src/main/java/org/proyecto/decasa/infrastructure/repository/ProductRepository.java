package org.proyecto.decasa.infrastructure.repository;

import org.proyecto.decasa.domain.entity.Product;
import org.proyecto.decasa.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Menú público: solo disponibles y no exclusivos, ordenados por categoría
    // "exclusive = false" excluye el tartar y los encargos especiales del menú general
    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.category c
            WHERE p.available = true
              AND p.exclusive = false
            ORDER BY c.sortOrder ASC, p.name ASC
            """)
    List<Product> findPublicMenu();

    // Filtra por categoría — usado cuando el frontend pide solo un tipo de plato
    // JOIN FETCH evita el problema N+1: carga la categoría en la misma query
    @Query("""
            SELECT p FROM Product p
            JOIN FETCH p.category c
            WHERE c.name = :categoryType
              AND p.available = true
              AND p.exclusive = false
            ORDER BY p.name ASC
            """)
    List<Product> findByCategoryTypeAndAvailable(CategoryType categoryType);

    // Admin: ve todos los productos incluyendo desactivados y exclusivos
    @Query("SELECT p FROM Product p JOIN FETCH p.category c ORDER BY c.sortOrder ASC, p.name ASC")
    List<Product> findAllWithCategory();
}
