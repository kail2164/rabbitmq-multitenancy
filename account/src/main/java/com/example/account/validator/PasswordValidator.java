package com.example.account.validator;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

public class PasswordValidator {	

	public static boolean isPasswordLengthSecured(String pwd) {
		return pwd.length() > 8;
	}

	public static void validatePassword(String pwd) throws CustomException {
		if (pwd == null || !PasswordValidator.isPasswordLengthSecured(pwd)) {
			throw new CustomException(APIStatus.BAD_REQUEST, "Password length must be longer than 8 characters");
		}
	}
}
