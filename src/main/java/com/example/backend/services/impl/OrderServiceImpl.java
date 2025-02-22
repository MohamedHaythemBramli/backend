package com.example.backend.services.impl;

import com.example.backend.dto.Pagination;
import com.example.backend.dto.WebResponse;
import com.example.backend.dto.order.CreateOrderRequest;
import com.example.backend.dto.order.GetListOrderRequest;
import com.example.backend.dto.order.OrderDTO;
import com.example.backend.dto.order.UpdateOrderRequest;
import com.example.backend.entities.CustomerEntity;
import com.example.backend.entities.ItemEntity;
import com.example.backend.entities.OrderEntity;
import com.example.backend.repositories.CustomerEntityRepository;
import com.example.backend.repositories.ItemEntityRepository;
import com.example.backend.repositories.OrderEntityRepository;
import com.example.backend.repositories.specification.OrderSpecification;
import com.example.backend.services.OrderService;
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
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderEntityRepository orderRepository;

    @Autowired
    private ItemEntityRepository itemRepository;

    @Autowired
    private CustomerEntityRepository customerRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ValidationService validator;

    @Override
    public ResponseEntity<WebResponse<Object>> createOrder(CreateOrderRequest request) {
        validator.validate(request);
        try {
            String orderCode = "";
            Integer totalPrice = 0;

            if (!customerRepository.existsById(request.getCustomerID())) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.CUSTOMER_ID_INVALID));
            }

            if (!itemRepository.existsById(request.getItemsID())) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID));
            }
            if (request.getQuantity() <= 0) {
                return ResponseUtils
                        .error400Response(Constants.getMessage(messageSource, Constants.QUANTITY_INVALID));
            }

            CustomerEntity customer = customerRepository.findById(request.getCustomerID()).get();
            ItemEntity item = itemRepository.findById(request.getItemsID()).get();
            totalPrice = item.getPrice() * request.getQuantity();
            orderCode = String.format("%s-%s", customer.getCustomerCode(), Instant.now().toEpochMilli());

            OrderEntity order = OrderEntity.builder()
                    .orderCode(orderCode)
                    .orderDate(LocalDate.now())
                    .totalPrice(totalPrice)
                    .quantity(request.getQuantity())
                    .customer(customer)
                    .item(item)
                    .build();

            orderRepository.save(order);

            OrderDTO result = OrderDTO
                    .builder()
                    .orderId(order.getOrderID())
                    .orderCode(order.getOrderCode())
                    .orderDate(order.getOrderDate())
                    .totalPrice(order.getTotalPrice())
                    .quantity(order.getQuantity())
                    .customer(customer)
                    .item(item)
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ORDER_CREATED_SUCCESS),
                    order.getOrderCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> updateOrder(UpdateOrderRequest request) {
        validator.validate(request);
        try {
            String orderCode = "";
            Integer totalPrice = 0;

            if (!orderRepository.existsById(request.getOrderID())) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ORDER_ID_INVALID));
            }

            if (!customerRepository.existsById(request.getCustomerID())) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.CUSTOMER_ID_INVALID));
            }

            if (!itemRepository.existsById(request.getItemsID())) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID));
            }
            if (request.getQuantity() <= 0) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.QUANTITY_INVALID));
            }

            CustomerEntity customer = customerRepository.findById(request.getCustomerID()).get();
            ItemEntity item = itemRepository.findById(request.getItemsID()).get();
            OrderEntity order = orderRepository.findById(request.getOrderID()).get();
            totalPrice = item.getPrice() * request.getQuantity();
            orderCode = order.getOrderCode();

            orderRepository.updateOrder(orderCode, order.getOrderDate(), totalPrice, customer.getCustomerID(),
                    request.getItemsID(), request.getQuantity(), order.getOrderID());

            OrderDTO result = OrderDTO
                    .builder()
                    .orderId(order.getOrderID())
                    .orderCode(order.getOrderCode())
                    .orderDate(order.getOrderDate())
                    .totalPrice(totalPrice)
                    .quantity(request.getQuantity())
                    .customer(customer)
                    .item(item)
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ORDER_UPDATED_SUCCESS),
                    order.getOrderCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> findOrder(Integer orderID) {
        try {
            Optional<OrderEntity> existingOrder = orderRepository.findById(orderID);
            if (!existingOrder.isPresent()) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ITEMS_ID_INVALID));
            }

            OrderDTO result = OrderDTO
                    .builder()
                    .orderId(existingOrder.get().getOrderID())
                    .orderCode(existingOrder.get().getOrderCode())
                    .orderDate(existingOrder.get().getOrderDate())
                    .totalPrice(existingOrder.get().getTotalPrice())
                    .quantity(existingOrder.get().getQuantity())
                    .customer(existingOrder.get().getCustomer())
                    .item(existingOrder.get().getItem())
                    .build();

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ORDER_RETRIEVED_SUCCESS),
                    existingOrder.get().getOrderCode());

            return ResponseUtils.success200Response(message, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> deleteOrder(Integer orderID) {
        try {
            Optional<OrderEntity> existingOrder = orderRepository.findById(orderID);
            if (!existingOrder.isPresent()) {
                return ResponseUtils.error400Response(Constants.getMessage(messageSource, Constants.ORDER_ID_INVALID));
            }

            orderRepository.deleteById(orderID);

            String message = MessageFormat.format(Constants.getMessage(messageSource, Constants.ITEM_DELETED_SUCCESS),
                    existingOrder.get().getOrderCode());

            return ResponseUtils.success200Response(message, null);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<WebResponse<Object>> getOrder(GetListOrderRequest request) {
        validator.validate(request);
        try {
            int pageNumber = Integer.parseInt(request.getPageNumber());
            int pageSize = Integer.parseInt(request.getPageSize());
            List<OrderDTO> orders = new ArrayList<>();
            Sort.Direction sortDirection = request.getSortDirection()
                    .equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            OrderSpecification orderSpecification = new OrderSpecification(request);
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, "orderID"));
            Page<OrderEntity> itemPage = orderRepository.findAll(orderSpecification, pageable);
            for (OrderEntity order : itemPage) {
                orders.add(OrderDTO
                        .builder()
                        .orderId(order.getOrderID())
                        .orderCode(order.getOrderCode())
                        .orderDate(order.getOrderDate())
                        .totalPrice(order.getTotalPrice())
                        .quantity(order.getQuantity())
                        .customer(order.getCustomer())
                        .item(order.getItem())
                        .build());
            }

            Pagination<List<OrderDTO>> result = Pagination
                    .<List<OrderDTO>>builder()
                    .data(orders)
                    .totalPage(itemPage.getTotalPages())
                    .totalItems(itemPage.getTotalElements())
                    .build();

            return ResponseUtils.success200Response(Constants.getMessage(messageSource, Constants.ORDERS_RETRIEVED_SUCCESS),
                    result);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseUtils.error500Response(e.getMessage());
        }
    }

}