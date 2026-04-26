package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.*;
import org.proyecto.decasa.domain.enums.CategoryType;

import java.math.BigDecimal;

/**
 * DTO compartido para crear y actualizar productos (admin).
 *
 * Se usa el mismo record para POST y PUT.
 * En un PUT los campos podrían ser opcionales, pero para simplificar
 * el código mandamos siempre el objeto completo (full update, no patch parcial).
 */
public record ProductRequest(
        @NotNull(message = "La categoría es obligatoria") CategoryType categoryType,
        @NotBlank(message = "El nombre es obligatorio") String name,
        String description,
        @NotNull @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0") BigDecimal basePrice,
        @NotNull @Min(value = 1, message = "El mínimo de unidades debe ser al menos 1") Integer minimumUnits,
        boolean available,
        boolean exclusive,
        String imageUrl
) {}
