package com.example.backend.services;

import com.example.backend.dto.WebResponse;
import com.example.backend.dto.order.CreateOrderRequest;
import com.example.backend.dto.order.GetListOrderRequest;
import com.example.backend.dto.order.UpdateOrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<WebResponse<Object>> createOrder(CreateOrderRequest request);

    ResponseEntity<WebResponse<Object>> updateOrder(UpdateOrderRequest request);

    ResponseEntity<WebResponse<Object>> findOrder(Integer orderID);

    ResponseEntity<WebResponse<Object>> deleteOrder(Integer orderID);

    ResponseEntity<WebResponse<Object>> getOrder(GetListOrderRequest request);

}
