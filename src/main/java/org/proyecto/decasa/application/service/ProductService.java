package org.proyecto.decasa.application.service;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.ProductRequest;
import org.proyecto.decasa.api.dto.response.ProductResponse;
import org.proyecto.decasa.domain.entity.Category;
import org.proyecto.decasa.domain.entity.Product;
import org.proyecto.decasa.domain.enums.CategoryType;
import org.proyecto.decasa.exception.ResourceNotFoundException;
import org.proyecto.decasa.infrastructure.repository.CategoryRepository;
import org.proyecto.decasa.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // --- CONSULTAS PÚBLICAS ---

    @Transactional(readOnly = true)
    public List<ProductResponse> findPublicMenu() {
        return productRepository.findPublicMenu()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(CategoryType type) {
        return productRepository.findByCategoryTypeAndAvailable(type)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    // --- ADMIN ---

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAllWithCategory()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    /**
     * Crea un nuevo producto.
     * Primero buscamos la categoría — si no existe lanzamos 404 (no 500).
     * Luego construimos la entidad con el Builder de Lombok y la guardamos.
     */
    @Transactional
    public ProductResponse create(ProductRequest req) {
        Category category = resolveCategory(req.categoryType());

        Product product = Product.builder()
                .category(category)
                .name(req.name())
                .description(req.description())
                .basePrice(req.basePrice())
                .minimumUnits(req.minimumUnits())
                .available(req.available())
                .exclusive(req.exclusive())
                .imageUrl(req.imageUrl())
                .build();

        return ProductResponse.from(productRepository.save(product));
    }

    /**
     * Actualiza todos los campos de un producto existente (full update).
     * Cargamos la entidad, modificamos sus campos y dejamos que JPA
     * detecte los cambios al cerrar la transacción (@Transactional flush automático).
     */
    @Transactional
    public ProductResponse update(Long id, ProductRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));

        Category category = resolveCategory(req.categoryType());

        product.setCategory(category);
        product.setName(req.name());
        product.setDescription(req.description());
        product.setBasePrice(req.basePrice());
        product.setMinimumUnits(req.minimumUnits());
        product.setAvailable(req.available());
        product.setExclusive(req.exclusive());
        product.setImageUrl(req.imageUrl());

        // No hace falta llamar a save() — JPA detecta el cambio al cerrar la transacción (dirty checking)
        return ProductResponse.from(product);
    }

    /**
     * Soft delete: en lugar de borrar el registro, lo marcamos como no disponible.
     * Así conservamos el histórico en pedidos existentes (que tienen snapshot del nombre/precio).
     */
    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
        product.setAvailable(false);
    }

    private Category resolveCategory(CategoryType type) {
        return categoryRepository.findByName(type)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + type));
    }
}
