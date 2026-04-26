package org.proyecto.decasa.api.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.SetWeeklySpecialRequest;
import org.proyecto.decasa.api.dto.response.WeeklySpecialResponse;
import org.proyecto.decasa.application.service.WeeklySpecialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * El admin (Richi) establece qué es la tapa o mini de la semana.
 *
 * POST /api/admin/weekly-specials → establece el especial de la semana
 *
 * Si ya había un especial activo del mismo tipo esa semana,
 * el servicio lo desactiva automáticamente antes de crear el nuevo.
 */
@RestController
@RequestMapping("/api/admin/weekly-specials")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminWeeklySpecialController {

    private final WeeklySpecialService weeklySpecialService;

    @PostMapping
    public ResponseEntity<WeeklySpecialResponse> set(@Valid @RequestBody SetWeeklySpecialRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weeklySpecialService.setWeeklySpecial(req));
    }
}
