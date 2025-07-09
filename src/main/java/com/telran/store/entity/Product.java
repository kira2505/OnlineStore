package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString(exclude = {"favorites", "category"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private BigDecimal discountPrice;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private List<Favorite> favorites;

    @ManyToOne
    private Category category;
}
