package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.enums.CategoryType;

import java.util.List;

/**
 * Una categoría con sus productos embebidos.
 * Se usa en el menú completo — así el frontend tiene todo en un objeto.
 */
public record CategoryResponse(
        Long id,
        CategoryType name,
        String displayName,
        Integer sortOrder,
        List<ProductResponse> products
) {}
