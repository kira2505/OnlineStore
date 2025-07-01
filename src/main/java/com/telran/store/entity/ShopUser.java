package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.telran.store.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShopUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shopUser")
    @JsonManagedReference
    private Set<Favorite> favorites;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shopUser")
    @JsonManagedReference
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
