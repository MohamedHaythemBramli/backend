package com.example.backend.repositories.specification;

import com.example.backend.dto.item.CreateItemRequest;
import com.example.backend.dto.item.GetListItemRequest;
import com.example.backend.entities.ItemEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSpecification implements Specification<ItemEntity> {

    private transient GetListItemRequest itemRequest;

    @Override
    @Nullable
    public Predicate toPredicate(Root<ItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        String itemsName = itemRequest.getItemName();
        String itemsCode = itemRequest.getItemCode();
        Boolean isAvailable = itemRequest.getIsAvailable();

        if (itemsName != null && !itemsName.isEmpty()) {
            Predicate itemsNameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("itemsName")),
                    String.format("%%%s%%", itemsName.toUpperCase()));
            predicates.add(itemsNameFilter);
        }

        if (itemsCode != null && !itemsCode.isEmpty()) {
            Predicate itemsCodeFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("itemsCode")),
                    String.format("%%%s%%", itemsCode.toUpperCase()));
            predicates.add(itemsCodeFilter);
        }

        if (isAvailable != null) {
            Predicate isAvailableFilter = criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
            predicates.add(isAvailableFilter);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }
}
