package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@ToString(exclude = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @EqualsAndHashCode.Include
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    @EqualsAndHashCode.Include
    private Product product;

    private Integer quantity;

    private BigDecimal price;

    public CartItem(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
