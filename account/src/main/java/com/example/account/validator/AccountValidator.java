package com.example.account.validator;

import com.example.account.model.Account;
import com.example.common.dto.CustomException;
import com.example.common.util.StringUtil;
import com.example.common.validator.StringValidator;

public class AccountValidator {
	public static void validateAccount(Account account) throws CustomException {
		StringValidator.validateNotNull(account.getUsername(), "Username cannot be null");
		StringValidator.validateNotNull(account.getRole(), "Role cannot be null");
		StringValidator.validateLength(account.getUsername(), 2, 255, "Username's length is from 2-255 characters");
		StringValidator.validateMaxLength(account.getRole(), 255, "Role max length is 255 characters");
		StringValidator.validateMaxLength(account.getFirstName(), 255, "First name max length is 255 characters");
		StringValidator.validateMaxLength(account.getLastName(), 255, "Last name max length is 255 characters");
	}
}
