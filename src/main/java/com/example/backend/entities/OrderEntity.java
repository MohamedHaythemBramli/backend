package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderID;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "items_id")
    private ItemEntity item;
}