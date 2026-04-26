package org.proyecto.decasa.api.controller;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.response.MenuResponse;
import org.proyecto.decasa.api.dto.response.PackResponse;
import org.proyecto.decasa.api.dto.response.ProductResponse;
import org.proyecto.decasa.api.dto.response.WeeklySpecialResponse;
import org.proyecto.decasa.application.service.PackService;
import org.proyecto.decasa.application.service.ProductService;
import org.proyecto.decasa.application.service.WeeklySpecialService;
import org.proyecto.decasa.domain.enums.CategoryType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GET /api/menu
 *
 * Endpoint principal del frontend — devuelve todo el menú en una sola llamada.
 * Angular lo usa al cargar la página principal para no hacer múltiples requests.
 *
 * @RestController = @Controller + @ResponseBody en cada método.
 * Serializa automáticamente los objetos Java a JSON.
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final ProductService productService;
    private final PackService packService;
    private final WeeklySpecialService weeklySpecialService;

    @GetMapping
    public ResponseEntity<MenuResponse> getMenu() {
        List<ProductResponse> allProducts = productService.findPublicMenu();
        List<PackResponse> packs = packService.findAvailable();
        List<WeeklySpecialResponse> specials = weeklySpecialService.findCurrentWeek();

        // Agrupamos productos por la etiqueta de su categoría (display name)
        // LinkedHashMap preserva el orden de inserción — importante para el menú
        Map<String, List<ProductResponse>> byCategory = allProducts.stream()
                .collect(Collectors.groupingBy(
                        ProductResponse::categoryDisplayName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return ResponseEntity.ok(new MenuResponse(byCategory, packs, specials));
    }
}
