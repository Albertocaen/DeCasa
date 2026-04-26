package org.proyecto.decasa.api.controller;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.response.PackResponse;
import org.proyecto.decasa.application.service.PackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** GET /api/packs y GET /api/packs/{id} — público */
@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class PackController {

    private final PackService packService;

    @GetMapping
    public ResponseEntity<List<PackResponse>> getAll() {
        return ResponseEntity.ok(packService.findAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packService.findById(id));
    }
}
