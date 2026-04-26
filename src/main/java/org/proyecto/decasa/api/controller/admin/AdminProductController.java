package org.proyecto.decasa.api.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.ProductRequest;
import org.proyecto.decasa.api.dto.response.ProductResponse;
import org.proyecto.decasa.application.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestión de productos — solo accesible con JWT de admin.
 *
 * La seguridad ya está garantizada por SecurityConfig (/api/admin/** → ROLE_ADMIN),
 * pero añadimos @PreAuthorize como segunda capa de defensa.
 * Es buena práctica en seguridad: defense in depth.
 *
 * GET    /api/admin/products         → todos los productos (incluso desactivados)
 * POST   /api/admin/products         → crear producto
 * PUT    /api/admin/products/{id}    → actualizar producto
 * DELETE /api/admin/products/{id}    → desactivar producto (soft delete)
 */
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.update(id, req));
    }

    /**
     * Soft delete — no borra el registro de la BD.
     * Esto es importante porque los pedidos antiguos tienen snapshot
     * del nombre y precio: si borráramos el producto perderíamos contexto histórico.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
