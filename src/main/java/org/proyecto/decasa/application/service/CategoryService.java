package org.proyecto.decasa.application.service;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.response.CategoryResponse;
import org.proyecto.decasa.api.dto.response.ProductResponse;
import org.proyecto.decasa.domain.entity.Category;
import org.proyecto.decasa.infrastructure.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Service le dice a Spring que este es un componente de lógica de negocio.
 * Spring lo registra como un bean y lo inyecta donde se necesite con @Autowired / constructor.
 *
 * @RequiredArgsConstructor (Lombok) genera el constructor con todos los campos final.
 * Es la forma recomendada de hacer inyección de dependencias en Spring —
 * más testeable que @Autowired en el campo.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * @Transactional(readOnly = true) abre una transacción de solo lectura.
     * Ventaja: Hibernate puede optimizar las queries (no hace flush al cerrar).
     * Siempre usar readOnly=true en métodos que no modifican datos.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CategoryResponse toResponse(Category c) {
        List<ProductResponse> products = c.getProducts()
                .stream()
                .filter(p -> p.getAvailable() && !p.getExclusive())
                .map(ProductResponse::from)
                .toList();

        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getDisplayName(),
                c.getSortOrder(),
                products
        );
    }
}
