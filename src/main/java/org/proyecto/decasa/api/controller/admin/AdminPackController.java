package org.proyecto.decasa.api.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.PackRequest;
import org.proyecto.decasa.api.dto.response.PackResponse;
import org.proyecto.decasa.application.service.PackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Gestión de packs para eventos (admin).
 *
 * POST /api/admin/packs      → crear pack
 * PUT  /api/admin/packs/{id} → actualizar pack
 */
@RestController
@RequestMapping("/api/admin/packs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPackController {

    private final PackService packService;

    @PostMapping
    public ResponseEntity<PackResponse> create(@Valid @RequestBody PackRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody PackRequest req) {
        return ResponseEntity.ok(packService.update(id, req));
    }
}
