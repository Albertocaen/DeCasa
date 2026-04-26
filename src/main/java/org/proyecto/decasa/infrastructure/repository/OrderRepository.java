package org.proyecto.decasa.infrastructure.repository;

import org.proyecto.decasa.domain.entity.Order;
import org.proyecto.decasa.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // El cliente consulta su pedido por el número legible (ej: DC-20260426-0001)
    @Query("""
            SELECT o FROM Order o
            LEFT JOIN FETCH o.items i
            LEFT JOIN FETCH i.product
            LEFT JOIN FETCH i.pack
            WHERE o.orderNumber = :orderNumber
            """)
    Optional<Order> findByOrderNumber(String orderNumber);

    // Admin: pedidos por estado (ej: ver todos los PENDING)
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    // Admin: todos los pedidos más recientes primero
    List<Order> findAllByOrderByCreatedAtDesc();

    // Cuenta pedidos de hoy — se usa para generar el número de pedido correlativo
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :from AND o.createdAt < :to")
    long countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
