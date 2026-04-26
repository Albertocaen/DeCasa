package org.proyecto.decasa.api.controller;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.response.WeeklySpecialResponse;
import org.proyecto.decasa.application.service.WeeklySpecialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** GET /api/weekly-specials/current — devuelve la tapa y mini de esta semana */
@RestController
@RequestMapping("/api/weekly-specials")
@RequiredArgsConstructor
public class WeeklySpecialController {

    private final WeeklySpecialService weeklySpecialService;

    @GetMapping("/current")
    public ResponseEntity<List<WeeklySpecialResponse>> getCurrentWeek() {
        return ResponseEntity.ok(weeklySpecialService.findCurrentWeek());
    }
}
