package org.proyecto.decasa.api.controller;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.response.ProductResponse;
import org.proyecto.decasa.application.service.ProductService;
import org.proyecto.decasa.domain.enums.CategoryType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints públicos de productos — sin autenticación.
 *
 * GET /api/products              → menú completo (no exclusivos)
 * GET /api/products/{id}         → producto concreto
 * GET /api/products/category/{type} → filtrado por categoría
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findPublicMenu());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * @PathVariable extrae el valor de la URL.
     * CategoryType es un enum — Spring lo convierte automáticamente del string de la URL.
     * Si el valor no coincide con ningún enum, Spring devuelve 400.
     */
    @GetMapping("/category/{type}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable CategoryType type) {
        return ResponseEntity.ok(productService.findByCategory(type));
    }
}
