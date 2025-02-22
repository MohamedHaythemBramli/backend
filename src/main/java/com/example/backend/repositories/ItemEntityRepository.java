package com.example.backend.repositories;

import com.example.backend.entities.ItemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ItemEntityRepository extends JpaRepository<ItemEntity, Integer>, JpaSpecificationExecutor<ItemEntity> {
    boolean existsByItemsCodeAndItemsIDNot(String itemsCode, Integer itemsID);

    boolean existsByItemsCode(String itemsCode);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE items SET items_name = :itemsName, items_code = :itemsCode, stock = :stock, price = :price, is_available = :isAvailable, last_re_stock = :lastReStock WHERE items_id = :itemsID")
    void updateItem(
            @Param("itemsName") String itemsName,
            @Param("itemsCode") String itemsCode,
            @Param("stock") Integer stock,
            @Param("price") Integer price,
            @Param("isAvailable") Boolean isAvailable,
            @Param("lastReStock") LocalDate lastReStock,
            @Param("itemsID") Integer itemsID);
}