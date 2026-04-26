package org.proyecto.decasa.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pack")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_min", nullable = false, precision = 8, scale = 2)
    private BigDecimal priceMin;

    // Null en packs de precio fijo (XL = €200)
    @Column(name = "price_max", precision = 8, scale = 2)
    private BigDecimal priceMax;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "pack", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PackComponent> components = new ArrayList<>();
}
