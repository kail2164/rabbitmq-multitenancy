package com.example.product.validator;

import com.example.common.dto.CustomException;
import com.example.common.validator.StringValidator;
import com.example.product.model.Product;

public class ProductValidator {
	public static void validateProduct(Product product, boolean isCreate) throws CustomException {
		String imgUrl = product.getImgUrl();
		String name = product.getName();
		if(isCreate) {
			StringValidator.validateNotNull(imgUrl, "Image URL can't be null or empty");
			StringValidator.validateNotNull(name, "Product's name can't be null or empty");
		}
		if(imgUrl != null) {
			StringValidator.validateLength(imgUrl, 2, 255, "Image URL length is from 2-255 characters");
		}
		if(name != null) {
			StringValidator.validateLength(name, 2, 255, "Product's name length is from 2-255 characters");
		}				
	}	
	
}
