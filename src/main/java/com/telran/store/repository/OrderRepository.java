package com.telran.store.repository;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.entity.Order;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByShopUserId(Long userId);

    List<Order> findAllByStatus(Status status);

    List<Order> findAllByPaymentStatus(PaymentStatus paymentStatus);

    @Query("""
            SELECT new com.telran.store.dto.ProductSalesDTO(p.name, SUM(oi.quantity))
            FROM Order o
            JOIN o.orderItems oi
            JOIN oi.product p
            WHERE o.status = :status
            GROUP BY p.name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<ProductSalesDTO> findProductSalesByStatus(@Param("status") Status status);

    @Query("""
            SELECT (SUM(o.totalAmount))
            FROM Order o
            WHERE o.status = 'COMPLETED' AND o.createdAt >= :date
            """)
    BigDecimal getTotalProfit(@Param("date") LocalDateTime date);

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.paymentStatus = 'PENDING_PAID'
                        AND o.createdAt < :cutoffDate
            """)
    List<Order> findPendingPaymentOrderThen(@Param("cutoffDate") LocalDateTime cutoffDate);
}
