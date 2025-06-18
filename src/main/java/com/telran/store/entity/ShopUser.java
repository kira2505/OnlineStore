package com.telran.store.entity;

import com.telran.store.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "shop_users")
public class ShopUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;
}
