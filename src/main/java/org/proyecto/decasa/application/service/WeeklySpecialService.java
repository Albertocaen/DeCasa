package org.proyecto.decasa.application.service;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.SetWeeklySpecialRequest;
import org.proyecto.decasa.api.dto.response.WeeklySpecialResponse;
import org.proyecto.decasa.domain.entity.Product;
import org.proyecto.decasa.domain.entity.WeeklySpecial;
import org.proyecto.decasa.exception.BusinessRuleException;
import org.proyecto.decasa.exception.ResourceNotFoundException;
import org.proyecto.decasa.infrastructure.repository.ProductRepository;
import org.proyecto.decasa.infrastructure.repository.WeeklySpecialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklySpecialService {

    private final WeeklySpecialRepository weeklySpecialRepository;
    private final ProductRepository productRepository;

    /**
     * Devuelve los especiales de la semana actual.
     * El lunes de la semana se calcula aquí para que el frontend
     * no tenga que preocuparse por la lógica de calendario.
     */
    @Transactional(readOnly = true)
    public List<WeeklySpecialResponse> findCurrentWeek() {
        LocalDate mondayOfCurrentWeek = getCurrentMonday();
        return weeklySpecialRepository.findActiveByWeekOf(mondayOfCurrentWeek)
                .stream()
                .map(WeeklySpecialResponse::from)
                .toList();
    }

    /**
     * El admin establece el especial de la semana.
     * Si ya existía uno del mismo tipo esa semana, lo desactiva primero
     * (no queremos dos tapas de la semana activas a la vez).
     */
    @Transactional
    public WeeklySpecialResponse setWeeklySpecial(SetWeeklySpecialRequest req) {
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + req.productId()));

        if (!product.getAvailable()) {
            throw new BusinessRuleException("No se puede usar un producto no disponible como especial de la semana");
        }

        // Desactiva el especial anterior del mismo tipo en esa semana si existe
        weeklySpecialRepository.findByTypeAndWeekOf(req.type(), req.weekOf())
                .ifPresent(existing -> existing.setActive(false));

        WeeklySpecial special = WeeklySpecial.builder()
                .product(product)
                .type(req.type())
                .weekOf(req.weekOf())
                .active(true)
                .build();

        return WeeklySpecialResponse.from(weeklySpecialRepository.save(special));
    }

    /** Calcula el lunes de la semana en curso usando la API de tiempo de Java. */
    private LocalDate getCurrentMonday() {
        LocalDate today = LocalDate.now();
        // DayOfWeek.MONDAY.getValue() = 1, por eso restamos (dayOfWeek - 1)
        return today.minusDays(today.getDayOfWeek().getValue() - 1);
    }
}
