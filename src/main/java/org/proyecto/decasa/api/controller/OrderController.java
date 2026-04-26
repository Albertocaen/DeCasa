package org.proyecto.decasa.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.PlaceOrderRequest;
import org.proyecto.decasa.api.dto.response.OrderResponse;
import org.proyecto.decasa.application.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de pedidos accesibles por el cliente (sin JWT).
 *
 * POST /api/orders             → crear un pedido
 * GET  /api/orders/{number}   → consultar mi pedido por número
 *
 * @Valid activa la validación del DTO — si falla, GlobalExceptionHandler devuelve 400.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest req) {
        OrderResponse order = orderService.placeOrder(req);
        // 201 Created es más semántico que 200 OK para recursos recién creados
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponse> trackOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.findByOrderNumber(orderNumber));
    }
}
