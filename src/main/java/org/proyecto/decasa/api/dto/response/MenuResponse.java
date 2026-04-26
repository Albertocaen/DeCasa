package org.proyecto.decasa.api.dto.response;

import java.util.List;
import java.util.Map;

/**
 * Respuesta del endpoint GET /api/menu.
 *
 * Devuelve todo lo necesario para renderizar el menú en una sola llamada:
 * - productos agrupados por categoría
 * - packs para eventos
 * - especiales de la semana actual
 *
 * Esto evita que el frontend haga 4 llamadas separadas al cargar la página.
 */
public record MenuResponse(
        Map<String, List<ProductResponse>> productsByCategory,
        List<PackResponse> packs,
        List<WeeklySpecialResponse> weeklySpecials
) {}
