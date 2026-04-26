package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.entity.Product;
import org.proyecto.decasa.domain.enums.CategoryType;

import java.math.BigDecimal;

/**
 * Lo que el API devuelve al cliente sobre un producto.
 *
 * No exponemos la entidad JPA directamente por dos razones:
 *   1. Seguridad: evitar exponer campos internos (createdAt, etc.)
 *   2. Desacoplamiento: podemos cambiar la entidad sin romper el contrato del API.
 *
 * El método estático from() convierte la entidad al DTO — es el "mapper" manual.
 */
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal basePrice,
        Integer minimumUnits,
        boolean available,
        boolean exclusive,
        String imageUrl,
        CategoryType categoryType,
        String categoryDisplayName
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getBasePrice(),
                p.getMinimumUnits(),
                p.getAvailable(),
                p.getExclusive(),
                p.getImageUrl(),
                p.getCategory().getName(),
                p.getCategory().getDisplayName()
        );
    }
}
