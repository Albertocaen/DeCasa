package org.proyecto.decasa.api.dto.response;

import org.proyecto.decasa.domain.entity.Pack;

import java.math.BigDecimal;
import java.util.List;

public record PackResponse(
        Long id,
        String name,
        String description,
        BigDecimal priceMin,
        BigDecimal priceMax,   // null = precio fijo (ver priceMin)
        boolean available,
        List<PackComponentResponse> components
) {
    public static PackResponse from(Pack p) {
        List<PackComponentResponse> comps = p.getComponents()
                .stream()
                .map(PackComponentResponse::from)
                .toList();

        return new PackResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPriceMin(),
                p.getPriceMax(),
                p.getAvailable(),
                comps
        );
    }
}
