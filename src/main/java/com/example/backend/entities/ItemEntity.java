package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    private Integer itemsID;

    @Column(name = "items_name")
    private String itemsName;

    @Column(name = "items_code")
    private String itemsCode;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Integer price;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    private LocalDate lastRestock;

}
