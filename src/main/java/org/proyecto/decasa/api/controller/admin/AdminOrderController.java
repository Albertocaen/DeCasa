package org.proyecto.decasa.api.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.UpdateOrderStatusRequest;
import org.proyecto.decasa.api.dto.response.OrderResponse;
import org.proyecto.decasa.application.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Panel de pedidos para el admin (Richi, la hija, Sergio).
 *
 * GET   /api/admin/orders          → todos los pedidos (más reciente primero)
 * GET   /api/admin/orders/{id}     → detalle de un pedido
 * PATCH /api/admin/orders/{id}/status → actualizar estado
 *
 * PATCH es más semántico que PUT aquí porque solo actualizamos un campo,
 * no el recurso entero.
 */
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateOrderStatusRequest req) {
        return ResponseEntity.ok(orderService.updateStatus(id, req));
    }
}
