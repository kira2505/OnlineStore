package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.telran.store.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private Set<Favorite> favorites;
}
