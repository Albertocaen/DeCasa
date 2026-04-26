package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.proyecto.decasa.domain.enums.OrderItemType;

/**
 * Una línea del pedido que manda el cliente.
 *
 * type  → PRODUCT o PACK
 * itemId → el ID del producto o del pack según el type
 * quantity → cuántos quiere
 *
 * La validación de mínimos por producto (ej: mínimo 6 croquetas)
 * se hace en OrderService, no aquí, porque requiere consultar la BD.
 */
public record OrderItemRequest(
        @NotNull(message = "El tipo de ítem es obligatorio") OrderItemType type,
        @NotNull(message = "El ID del producto o pack es obligatorio") Long itemId,
        @NotNull @Min(value = 1, message = "La cantidad debe ser al menos 1") Integer quantity
) {}
