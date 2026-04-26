package org.proyecto.decasa.infrastructure.repository;

import org.proyecto.decasa.domain.entity.WeeklySpecial;
import org.proyecto.decasa.domain.enums.WeeklySpecialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklySpecialRepository extends JpaRepository<WeeklySpecial, Long> {

    // Especiales activos de una semana concreta (pasamos el lunes de esa semana)
    @Query("""
            SELECT ws FROM WeeklySpecial ws
            JOIN FETCH ws.product p
            JOIN FETCH p.category
            WHERE ws.weekOf = :weekOf AND ws.active = true
            """)
    List<WeeklySpecial> findActiveByWeekOf(LocalDate weekOf);

    // Busca si ya existe un especial del mismo tipo en esa semana
    // — la DB tiene UNIQUE(type, week_of), esta query lo valida antes de insertar
    Optional<WeeklySpecial> findByTypeAndWeekOf(WeeklySpecialType type, LocalDate weekOf);
}
