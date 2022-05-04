package com.example.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.common.dto.CustomException;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;
import com.example.product.converter.ProductConverter;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.product.validator.ProductValidator;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Override
	public List<ProductResponse> getProducts() throws CustomException {
		return ProductConverter.convertToListResponse(productRepository.findAll());
	}

	@Override
	public ProductResponse create(ProductRequest request) throws CustomException {
		Product product = ProductConverter.convertFromRequest(request);
		ProductValidator.validateProduct(product);
		product = productRepository.save(product);		
		return ProductConverter.convertToResponse(product);
	}

}
