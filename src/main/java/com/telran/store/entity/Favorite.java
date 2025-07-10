package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@ToString(exclude = "products")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "shop_user_id"}))
@EqualsAndHashCode
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product products;

    @ManyToOne
    @JoinColumn(name = "shop_user_id")
    @JsonBackReference
    private ShopUser shopUser;

    public Favorite(Product products, ShopUser shopUser) {
        this.products = products;
        this.shopUser = shopUser;
    }
}