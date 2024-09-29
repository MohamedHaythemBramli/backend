package com.example.backend.services.impl;

import com.example.backend.dto.Pagination;
import com.example.backend.dto.WebResponse;
import com.example.backend.dto.item.CreateItemRequest;
import com.example.backend.dto.item.GetListItemRequest;
import com.example.backend.dto.item.ItemDTO;
import com.example.backend.dto.item.UpdateItemRequest;
import com.example.backend.entities.ItemEntity;
import com.example.backend.repositories.ItemEntityRepository;
import com.example.backend.repositories.specification.ItemSpecification;
import com.example.backend.services.ItemService;
import com.example.backend.utilities.Constants;
import com.example.backend.utilities.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemEntityRepository itemRepository;

    @Autowired
    private ValidationService validator;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseEntity<WebResponse<Object>> createItem(CreateItemRequest request) {
        validator.validate(request);
        try {
            if (itemRepository.existsByItemsCode(request.getCode())) {
                return ResponseUtils
                        .errorValidationResponse(
                                String.format("code: %s", Constants.getMessage(messageSource, Constants.ITEM_CODE_IS_EXIST)));
            }

            ItemEntity item = ItemEntity.builder()
                    .itemsName(request.getName())
                    .itemsCode(request.getCode())
                    .price(request.getPrice())
                    .isAvailable(request.getIsAvailable())
                    .stock(request.getStock())
                    .build();

            itemRepository.save(item);

            ItemDTO result = ItemDTO
                    .builder()
                    .itemsID(item.getItemsID())
                    .itemsName(item.getItemsName())
                    .itemsCode(item.getItemsCode())
                    .stock(item.getStock())
                    .price(item.getPrice())
                    .isAvailable(item.getIsAvailable())
                    .lastReStock(item.getLastRestock())
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ITEM_CREATED_SUCCESS),
                    request.getCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> updateItem(UpdateItemRequest request) {
        validator.validate(request);
        try {
            LocalDate lastReStock = null;
            Integer itemsID = request.getItemsID();
            Optional<ItemEntity> existingItem = itemRepository.findById(itemsID);
            if (!existingItem.isPresent()) {
                return ResponseUtils.errorValidationResponse(
                        String.format("code: %s", Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID)));
            }
            lastReStock = existingItem.get().getLastRestock();

            if (itemRepository.existsByItemsCodeAndItemsIDNot(request.getCode(), itemsID)) {
                return ResponseUtils
                        .errorValidationResponse(
                                String.format("code: %s", Constants.getMessage(messageSource, Constants.ITEM_CODE_IS_EXIST)));
            }

            if (request.getLastReStock() != null) {
                lastReStock = request.getLastReStock();
            }

            itemRepository.updateItem(request.getName(), request.getCode(), request.getStock(), request.getPrice(),
                    request.getIsAvailable(), lastReStock, itemsID);

            ItemDTO result = ItemDTO
                    .builder()
                    .itemsID(itemsID)
                    .itemsName(request.getName())
                    .itemsCode(request.getCode())
                    .stock(request.getStock())
                    .price(request.getPrice())
                    .isAvailable(request.getIsAvailable())
                    .lastReStock(lastReStock)
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ITEM_UPDATED_SUCCESS),
                    request.getCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> findItem(Integer itemID) {
        try {
            Optional<ItemEntity> existingItem = itemRepository.findById(itemID);
            if (!existingItem.isPresent()) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID));
            }

            ItemDTO result = ItemDTO
                    .builder()
                    .itemsID(existingItem.get().getItemsID())
                    .itemsName(existingItem.get().getItemsName())
                    .itemsCode(existingItem.get().getItemsCode())
                    .stock(existingItem.get().getStock())
                    .price(existingItem.get().getPrice())
                    .isAvailable(existingItem.get().getIsAvailable())
                    .lastReStock(existingItem.get().getLastRestock())
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ITEM_RETRIEVED_SUCCESS),
                    existingItem.get().getItemsCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> deleteItem(Integer itemID) {
        try {
            Optional<ItemEntity> existingItem = itemRepository.findById(itemID);
            if (!existingItem.isPresent()) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID));
            }

            itemRepository.deleteById(itemID);

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ITEM_DELETED_SUCCESS),
                    existingItem.get().getItemsCode());

            return ResponseUtils.success200Response(message, null);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> getItem(GetListItemRequest request) {
        validator.validate(request);
        try {
            int pageNumber = Integer.parseInt(request.getPageNumber());
            int pageSize = Integer.parseInt(request.getPageSize());
            List<ItemDTO> items = new ArrayList<>();
            Sort.Direction sortDirection = request.getSortDirection()
                    .equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            ItemSpecification itemSpecification = new ItemSpecification(request);
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, "itemsID"));
            Page<ItemEntity> itemPage = itemRepository.findAll(itemSpecification, pageable);
            for (ItemEntity item : itemPage) {
                items.add(ItemDTO
                        .builder()
                        .itemsID(item.getItemsID())
                        .itemsName(item.getItemsName())
                        .itemsCode(item.getItemsCode())
                        .stock(item.getStock())
                        .price(item.getPrice())
                        .isAvailable(item.getIsAvailable())
                        .lastReStock(item.getLastRestock())
                        .build());
            }

            Pagination<List<ItemDTO>> result = Pagination
                    .<List<ItemDTO>>builder()
                    .data(items)
                    .totalPage(itemPage.getTotalPages())
                    .totalItems(itemPage.getTotalElements())
                    .build();

            return ResponseUtils.success200Response(Constants.getMessage(messageSource, Constants.ITEMS_RETRIEVED_SUCCESS),
                    result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

}