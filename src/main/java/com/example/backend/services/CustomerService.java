package com.example.backend.services;

import com.example.backend.dto.WebResponse;
import com.example.backend.dto.customer.CreateCustomerRequest;
import com.example.backend.dto.customer.GetListCustomerRequest;
import com.example.backend.dto.customer.UpdateCustomerRequest;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<WebResponse<Object>> createCustomer(CreateCustomerRequest request);

    ResponseEntity<WebResponse<Object>> updateCustomer(UpdateCustomerRequest request);

    ResponseEntity<WebResponse<Object>> findCustomer(Integer customerID);

    ResponseEntity<WebResponse<Object>> deleteCustomer(Integer customerID);

    ResponseEntity<WebResponse<Object>> getCustomer(GetListCustomerRequest request);
}