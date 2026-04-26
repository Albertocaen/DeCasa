package org.proyecto.decasa.application.service;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.OrderItemRequest;
import org.proyecto.decasa.api.dto.request.PlaceOrderRequest;
import org.proyecto.decasa.api.dto.request.UpdateOrderStatusRequest;
import org.proyecto.decasa.api.dto.response.OrderResponse;
import org.proyecto.decasa.domain.entity.Order;
import org.proyecto.decasa.domain.entity.OrderItem;
import org.proyecto.decasa.domain.entity.Pack;
import org.proyecto.decasa.domain.entity.Product;
import org.proyecto.decasa.domain.enums.DeliveryType;
import org.proyecto.decasa.domain.enums.OrderItemType;
import org.proyecto.decasa.exception.BusinessRuleException;
import org.proyecto.decasa.exception.ResourceNotFoundException;
import org.proyecto.decasa.infrastructure.repository.OrderRepository;
import org.proyecto.decasa.infrastructure.repository.PackRepository;
import org.proyecto.decasa.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int MINIMUM_ADVANCE_HOURS = 48;
    private static final BigDecimal MINIMUM_DELIVERY_AMOUNT = new BigDecimal("40.00");
    private static final BigDecimal MINIMUM_PICKUP_AMOUNT   = new BigDecimal("20.00");

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PackRepository packRepository;

    // --- PEDIR ---

    /**
     * Flujo de creación de un pedido:
     *  1. Valida la fecha (mínimo 48h de antelación)
     *  2. Valida la dirección si es DELIVERY
     *  3. Construye los OrderItem con snapshot de precio
     *  4. Valida el importe mínimo
     *  5. Genera el número de pedido legible
     *  6. Guarda todo en una sola transacción (pedido + items)
     */
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest req) {
        validateDeliveryDate(req.deliveryDate());
        validateDeliveryAddress(req.deliveryType(), req.deliveryAddress());

        List<OrderItem> items = buildItems(req.items());
        BigDecimal subtotal = calculateSubtotal(items);

        validateMinimumAmount(req.deliveryType(), subtotal);

        String orderNumber = generateOrderNumber();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .customerName(req.customerName())
                .customerEmail(req.customerEmail())
                .customerPhone(req.customerPhone())
                .deliveryType(req.deliveryType())
                .deliveryAddress(req.deliveryAddress())
                .deliveryDate(req.deliveryDate())
                .notes(req.notes())
                .subtotal(subtotal)
                .deliveryFee(BigDecimal.ZERO)   // de momento sin coste de envío
                .total(subtotal)
                .build();

        // Vinculamos cada item a su pedido antes de guardar
        items.forEach(item -> item.setOrder(order));
        order.getItems().addAll(items);

        return OrderResponse.from(orderRepository.save(order));
    }

    // --- CONSULTAR ---

    @Transactional(readOnly = true)
    public OrderResponse findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(OrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + orderNumber));
    }

    // --- ADMIN ---

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return orderRepository.findById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }

    /**
     * El admin cambia el estado del pedido (CONFIRMED, IN_PREPARATION, etc.).
     * No validamos transiciones de estado aquí para mantenerlo simple —
     * en una versión más robusta añadiríamos una máquina de estados.
     */
    @Transactional
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        order.setStatus(req.status());
        return OrderResponse.from(order);
    }

    // --- HELPERS PRIVADOS ---

    private void validateDeliveryDate(LocalDateTime deliveryDate) {
        LocalDateTime minAllowed = LocalDateTime.now().plusHours(MINIMUM_ADVANCE_HOURS);
        if (deliveryDate.isBefore(minAllowed)) {
            throw new BusinessRuleException(
                    "Los pedidos requieren al menos " + MINIMUM_ADVANCE_HOURS + " horas de antelación"
            );
        }
    }

    private void validateDeliveryAddress(DeliveryType type, String address) {
        if (type == DeliveryType.DELIVERY && (address == null || address.isBlank())) {
            throw new BusinessRuleException("La dirección de entrega es obligatoria para DELIVERY");
        }
    }

    private void validateMinimumAmount(DeliveryType type, BigDecimal subtotal) {
        BigDecimal minimum = type == DeliveryType.DELIVERY ? MINIMUM_DELIVERY_AMOUNT : MINIMUM_PICKUP_AMOUNT;
        if (subtotal.compareTo(minimum) < 0) {
            throw new BusinessRuleException(
                    "El pedido mínimo para " + type.name() + " es de €" + minimum
            );
        }
    }

    /**
     * Construye los OrderItem validando disponibilidad y mínimos.
     * Los precios se capturan en este momento (snapshot) para que
     * un cambio de precio futuro no afecte a este pedido.
     */
    private List<OrderItem> buildItems(List<OrderItemRequest> itemRequests) {
        List<OrderItem> result = new ArrayList<>();

        for (OrderItemRequest req : itemRequests) {
            if (req.type() == OrderItemType.PRODUCT) {
                result.add(buildProductItem(req));
            } else {
                result.add(buildPackItem(req));
            }
        }

        return result;
    }

    private OrderItem buildProductItem(OrderItemRequest req) {
        Product product = productRepository.findById(req.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + req.itemId()));

        if (!product.getAvailable()) {
            throw new BusinessRuleException("El producto '" + product.getName() + "' no está disponible");
        }

        // Validamos el mínimo de unidades (ej: mínimo 6 croquetas)
        if (req.quantity() < product.getMinimumUnits()) {
            throw new BusinessRuleException(
                    "El mínimo de '" + product.getName() + "' es " + product.getMinimumUnits() + " uds"
            );
        }

        BigDecimal subtotal = product.getBasePrice().multiply(BigDecimal.valueOf(req.quantity()));

        return OrderItem.builder()
                .itemType(OrderItemType.PRODUCT)
                .product(product)
                .itemName(product.getName())        // snapshot del nombre
                .unitPrice(product.getBasePrice())  // snapshot del precio
                .quantity(req.quantity())
                .subtotal(subtotal)
                .build();
    }

    private OrderItem buildPackItem(OrderItemRequest req) {
        Pack pack = packRepository.findById(req.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack no encontrado: " + req.itemId()));

        if (!pack.getAvailable()) {
            throw new BusinessRuleException("El pack '" + pack.getName() + "' no está disponible");
        }

        // Para los packs usamos el precio mínimo como referencia del snapshot
        BigDecimal unitPrice = pack.getPriceMin();
        BigDecimal subtotal  = unitPrice.multiply(BigDecimal.valueOf(req.quantity()));

        return OrderItem.builder()
                .itemType(OrderItemType.PACK)
                .pack(pack)
                .itemName(pack.getName())   // snapshot del nombre
                .unitPrice(unitPrice)       // snapshot del precio mínimo
                .quantity(req.quantity())
                .subtotal(subtotal)
                .build();
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Genera un número de pedido legible: DC-YYYYMMDD-XXXX
     * Ejemplo: DC-20260426-0003
     *
     * Contamos los pedidos de hoy para generar el correlativo.
     * En producción con alta concurrencia habría que usar una secuencia de BD,
     * pero para este proyecto es suficiente.
     */
    private String generateOrderNumber() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay   = LocalDate.now().atTime(LocalTime.MAX);

        long todayCount = orderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("DC-%s-%04d", date, todayCount + 1);
    }
}
