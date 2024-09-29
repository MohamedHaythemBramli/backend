package com.example.backend.services;

import com.example.backend.dto.WebResponse;
import com.example.backend.dto.item.CreateItemRequest;
import com.example.backend.dto.item.GetListItemRequest;
import com.example.backend.dto.item.UpdateItemRequest;
import org.springframework.http.ResponseEntity;

public interface ItemService {
    ResponseEntity<WebResponse<Object>> createItem(CreateItemRequest request);

    ResponseEntity<WebResponse<Object>> updateItem(UpdateItemRequest request);

    ResponseEntity<WebResponse<Object>> findItem(Integer itemID);

    ResponseEntity<WebResponse<Object>> deleteItem(Integer itemID);

    ResponseEntity<WebResponse<Object>> getItem(GetListItemRequest request);

}