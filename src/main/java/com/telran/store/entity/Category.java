package com.telran.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString(exclude = {"products"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Product> products;
}
