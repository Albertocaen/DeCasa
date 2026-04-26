package org.proyecto.decasa.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.proyecto.decasa.domain.enums.WeeklySpecialType;

import java.time.LocalDate;

@Entity
@Table(
    name = "weekly_special",
    uniqueConstraints = @UniqueConstraint(columnNames = {"type", "week_of"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySpecial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private WeeklySpecialType type;

    // Lunes de la semana a la que corresponde
    @Column(name = "week_of", nullable = false)
    private LocalDate weekOf;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
