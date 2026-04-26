package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Crear o actualizar un pack para eventos (admin).
 * price_max es nullable — el pack XL tiene precio fijo sin rango.
 */
public record PackRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal priceMin,
        BigDecimal priceMax,
        boolean available,
        int sortOrder
) {}
