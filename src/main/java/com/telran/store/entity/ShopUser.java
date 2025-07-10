package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.telran.store.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@ToString(exclude = {"favorites", "orders", "cart", "payments"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShopUser implements UserDetails {

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
    private Role role = Role.ROLE_USER;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
