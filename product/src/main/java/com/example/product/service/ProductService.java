package com.example.product.service;

import java.util.List;

import com.example.common.dto.CustomException;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;

public interface ProductService {
	List<ProductResponse> getProducts() throws CustomException;
	ProductResponse create(ProductRequest request) throws CustomException;
}
