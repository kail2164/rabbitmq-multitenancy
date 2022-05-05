package com.example.product.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.common.dto.APIStatus;
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
	public List<ProductResponse> getProducts(int page, int maxRecords) throws CustomException {
		PageRequest request = PageRequest.of(page, maxRecords);
		return ProductConverter.convertToListResponse(productRepository.findAll(request).getContent());
	}

	@Override
	public ProductResponse create(ProductRequest request) throws CustomException {
		Product product = ProductConverter.convertFromRequest(request);
		ProductValidator.validateProduct(product);
		try {
			product = productRepository.save(product);
			return ProductConverter.convertToResponse(product);
		} catch (Exception e) {
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
		
	}

	@Override
	public ProductResponse update(ProductRequest request) throws CustomException {
		Product product = ProductConverter.convertFromRequest(request);
		ProductValidator.validateProduct(product);
		try {
			Product productDB = productRepository.getById(request.getId());
			productDB = update(product, productDB);
			productDB = productRepository.save(product);
			return ProductConverter.convertToResponse(productDB);
		} catch (Exception e) {
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}		
	}

	@Override
	public Long delete(Long id) throws CustomException {
		try {
			productRepository.deleteById(id);
			return id;
		} catch (Exception e) {
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private boolean checkFieldEquals(Product newProduct, Product productToUpdate, String fieldName)
			throws CustomException {
		try {
			Field field = newProduct.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(newProduct).equals(field.get(productToUpdate));
		} catch (Exception e) {
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}

	}

	private Product update(Product newProduct, Product productToUpdate) throws CustomException {
		String name = checkFieldEquals(newProduct, productToUpdate, "name") ? productToUpdate.getName()
				: newProduct.getName();
		String imgUrl = checkFieldEquals(newProduct, productToUpdate, "imgUrl") ? productToUpdate.getImgUrl()
				: newProduct.getImgUrl();
		Double price = checkFieldEquals(newProduct, productToUpdate, "price") ? productToUpdate.getPrice()
				: newProduct.getPrice();
		productToUpdate.setImgUrl(imgUrl);
		productToUpdate.setName(name);
		productToUpdate.setPrice(price);
		return productToUpdate;
	}

}
