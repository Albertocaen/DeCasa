package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.entity.WeeklySpecial;
import org.proyecto.decasa.domain.enums.WeeklySpecialType;

import java.time.LocalDate;

/**
 * La tapa o mini de la semana.
 * El frontend la muestra resaltada en el menú (badge "Esta semana").
 */
public record WeeklySpecialResponse(
        Long id,
        WeeklySpecialType type,
        LocalDate weekOf,
        ProductResponse product
) {
    public static WeeklySpecialResponse from(WeeklySpecial ws) {
        return new WeeklySpecialResponse(
                ws.getId(),
                ws.getType(),
                ws.getWeekOf(),
                ProductResponse.from(ws.getProduct())
        );
    }
}
