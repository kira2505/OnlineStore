package com.telran.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString(exclude = {"user", "cartItems"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "shop_user_id")
    private ShopUser user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new HashSet<>();

    public Cart(ShopUser user) {
        this.user = user;
    }
}
