package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.entity.PackComponent;

/**
 * Un componente dentro de un pack (ej: "12 croquetas" o "18 tapas a elección del chef").
 * productId y productName son null cuando el componente es elección del chef.
 */
public record PackComponentResponse(
        Long productId,
        String productName,
        Integer quantity,
        String description
) {
    public static PackComponentResponse from(PackComponent c) {
        return new PackComponentResponse(
                c.getProduct() != null ? c.getProduct().getId() : null,
                c.getProduct() != null ? c.getProduct().getName() : null,
                c.getQuantity(),
                c.getDescription()
        );
    }
}
