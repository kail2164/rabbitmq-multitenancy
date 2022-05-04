package com.example.product.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;
import com.example.product.model.Product;

public class ProductConverter {
	public static Product convertFromRequest(ProductRequest request) {
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		product.setImgUrl(request.getImgUrl());
		product.setPrice(request.getPrice());
		return product;
	}

	public static ProductResponse convertToResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setImgUrl(product.getImgUrl());
		response.setName(product.getName());
		response.setPrice(product.getPrice());
		return response;
	}

	public static List<ProductResponse> convertToListResponse(List<Product> listProduct) {
		return listProduct.stream().map(data -> convertToResponse(data)).collect(Collectors.toList());
	}
}
