package org.proyecto.decasa.api.dto.request;

import jakarta.validation.constraints.NotNull;
import org.proyecto.decasa.domain.enums.OrderStatus;

/**
 * Cuerpo del PATCH /api/admin/orders/{id}/status.
 * El admin actualiza el estado del pedido (CONFIRMED, IN_PREPARATION, etc.)
 */
public record UpdateOrderStatusRequest(
        @NotNull(message = "El estado es obligatorio") OrderStatus status
) {}
