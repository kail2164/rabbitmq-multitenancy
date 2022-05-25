package com.example.product.service.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;
import com.example.common.util.ObjectUtils;
import com.example.product.converter.ProductConverter;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.product.validator.ProductValidator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@NoArgsConstructor
public class ProductServiceImpl implements ProductService {
	private ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<ProductResponse> getProducts(int page, int maxRecords) throws CustomException {
		PageRequest request = PageRequest.of(page, maxRecords);
		return ProductConverter.convertToListResponse(productRepository.findAll(request).getContent());
	}

	@Override
	public ProductResponse create(ProductRequest request) throws CustomException {
		Product product = ProductConverter.convertFromRequest(request);
		ProductValidator.validateProduct(product, true);
		try {
			product = productRepository.save(product);
			return ProductConverter.convertToResponse(product);
		} catch (Exception e) {
			log.error("Error in create: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@Override
	public ProductResponse update(ProductRequest request, boolean nullableUpdate) throws CustomException {
		Product product = ProductConverter.convertFromRequest(request);
		ProductValidator.validateProduct(product, false);
		if(request.getId() == null) {
			throw new CustomException(APIStatus.NOT_FOUND, "Product id cannot be null");
		}
		Product productDB = productRepository.getById(request.getId());
		if(productDB == null) {
			throw new CustomException(APIStatus.NOT_FOUND, "Not found product by id: " + request.getId());
		}
		productDB = update(product, productDB, nullableUpdate);
		try {			
			productDB = productRepository.save(product);
			return ProductConverter.convertToResponse(productDB);
		} catch (Exception e) {
			log.error("Error in update: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	public Long delete(Long id) throws CustomException {
		if(id == null) {
			throw new CustomException(APIStatus.NOT_FOUND, "Product id cannot be null");
		}
		productRepository.deleteById(id);
		return id;		
	}

	private Product update(Product newProduct, Product productToUpdate, boolean nullableUpdate) throws CustomException {
		productToUpdate = (Product) ObjectUtils.setFieldValue(newProduct, productToUpdate, "name", nullableUpdate);
		productToUpdate = (Product) ObjectUtils.setFieldValue(newProduct, productToUpdate, "imgUrl", nullableUpdate);
		productToUpdate = (Product) ObjectUtils.setFieldValue(newProduct, productToUpdate, "price", nullableUpdate);
		return productToUpdate;
	}

}
