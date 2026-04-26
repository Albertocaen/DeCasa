package org.proyecto.decasa.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pack_component")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pack_id", nullable = false)
    private Pack pack;

    // Null cuando el componente es "a elección del chef"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 200)
    private String description;
}
