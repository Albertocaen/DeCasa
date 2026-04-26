package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.NotNull;
import org.proyecto.decasa.domain.enums.WeeklySpecialType;

import java.time.LocalDate;

/**
 * El admin establece qué producto es la tapa/mini de la semana.
 *
 * weekOf debe ser el lunes de la semana (la DB tiene UNIQUE(type, week_of)).
 * El servicio valida que no haya ya un especial activo del mismo tipo esa semana.
 */
public record SetWeeklySpecialRequest(
        @NotNull Long productId,
        @NotNull WeeklySpecialType type,
        @NotNull LocalDate weekOf
) {}
