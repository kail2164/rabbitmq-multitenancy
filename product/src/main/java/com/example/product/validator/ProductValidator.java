package com.example.product.validator;

import com.example.common.dto.CustomException;
import com.example.common.validator.StringValidator;
import com.example.product.model.Product;

public class ProductValidator {
	public static void validateProduct(Product product, boolean isCreate) throws CustomException {
		if(isCreate) {
			StringValidator.validateNotNull(product.getImgUrl(), "Image URL can't be null or empty");
			StringValidator.validateNotNull(product.getName(), "Product's name can't be null or empty");
		}		
		StringValidator.validateLength(product.getImgUrl(), 2, 255, "Image URL length is from 2-255 characters");
		StringValidator.validateLength(product.getName(), 2, 255, "Product's name length is from 2-255 characters");
	}
	
	
}
