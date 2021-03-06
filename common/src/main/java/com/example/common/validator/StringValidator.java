package com.example.common.validator;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.StringUtils;

public class StringValidator {
	public static void validateNotNull(String field, String err) throws CustomException {
		if (StringUtils.isNullOrEmpty(field)) {
			throw new CustomException(APIStatus.BAD_REQUEST, err);
		}
	}

	public static void validateMinLength(String field, int min, String err) throws CustomException {
		if (field != null && field.length() < min) {
			throw new CustomException(APIStatus.BAD_REQUEST, err);
		}
	}

	public static void validateMaxLength(String field, int max, String err) throws CustomException {
		if (field != null &&field.length() > max) {
			throw new CustomException(APIStatus.BAD_REQUEST, err);
		}
	}

	public static void validateLength(String field, Integer min, Integer max, String err) throws CustomException {
		if (min != null) {
			validateMinLength(field, min, err);
		}
		if (max != null) {
			validateMaxLength(field, max, err);
		}
	}
}
