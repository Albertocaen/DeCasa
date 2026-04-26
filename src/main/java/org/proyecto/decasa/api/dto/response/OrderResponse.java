package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.entity.Order;
import org.proyecto.decasa.domain.entity.OrderItem;
import org.proyecto.decasa.domain.enums.DeliveryType;
import org.proyecto.decasa.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta completa de un pedido.
 * El cliente la recibe al hacer el pedido y al consultarlo por orderNumber.
 */
public record OrderResponse(
        Long id,
        String orderNumber,
        String customerName,
        String customerEmail,
        String customerPhone,
        DeliveryType deliveryType,
        String deliveryAddress,
        LocalDateTime deliveryDate,
        OrderStatus status,
        String notes,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {

    /** Línea del pedido — snapshot del precio en el momento del pedido. */
    public record OrderItemResponse(
            Long id,
            String itemType,
            Long productId,
            Long packId,
            String itemName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal
    ) {
        public static OrderItemResponse from(OrderItem i) {
            return new OrderItemResponse(
                    i.getId(),
                    i.getItemType().name(),
                    i.getProduct() != null ? i.getProduct().getId() : null,
                    i.getPack() != null ? i.getPack().getId() : null,
                    i.getItemName(),
                    i.getUnitPrice(),
                    i.getQuantity(),
                    i.getSubtotal()
            );
        }
    }

    public static OrderResponse from(Order o) {
        List<OrderItemResponse> itemResponses = o.getItems()
                .stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(
                o.getId(),
                o.getOrderNumber(),
                o.getCustomerName(),
                o.getCustomerEmail(),
                o.getCustomerPhone(),
                o.getDeliveryType(),
                o.getDeliveryAddress(),
                o.getDeliveryDate(),
                o.getStatus(),
                o.getNotes(),
                o.getSubtotal(),
                o.getDeliveryFee(),
                o.getTotal(),
                o.getCreatedAt(),
                itemResponses
        );
    }
}
