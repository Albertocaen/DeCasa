package org.proyecto.decasa.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.proyecto.decasa.domain.enums.DeliveryType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cuerpo de la petición POST /api/orders.
 *
 * @Valid en la lista de items hace que Spring también valide
 * cada OrderItemRequest dentro de la lista.
 *
 * La regla de 48h de antelación NO se valida aquí con @Future
 * porque @Future solo comprueba que sea futuro, no que sea 48h+.
 * Esa lógica va en OrderService.
 */
public record PlaceOrderRequest(
        @NotBlank(message = "El nombre es obligatorio") String customerName,
        @NotBlank @Email(message = "Email inválido") String customerEmail,
        @NotBlank(message = "El teléfono es obligatorio") String customerPhone,

        @NotNull(message = "Indica si es entrega o recogida") DeliveryType deliveryType,

        // Solo obligatorio si deliveryType = DELIVERY — validado en el servicio
        String deliveryAddress,

        @NotNull(message = "La fecha de entrega es obligatoria")
        @Future(message = "La fecha de entrega debe ser en el futuro") LocalDateTime deliveryDate,

        String notes,

        @NotEmpty(message = "El pedido debe tener al menos un producto")
        @Valid List<OrderItemRequest> items
) {}
