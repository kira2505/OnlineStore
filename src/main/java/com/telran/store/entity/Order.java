package com.telran.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString(exclude = {"shopUser", "payments", "orderItems"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_user_id")
    @JsonBackReference
    private ShopUser shopUser;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String deliveryAddress;

    private String contactPhone;

    private String deliveryMethod;

    @Enumerated(EnumType.STRING)
    private Status status;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    private BigDecimal paymentAmount = BigDecimal.ZERO;

    private BigDecimal totalAmount = BigDecimal.ZERO;
}
