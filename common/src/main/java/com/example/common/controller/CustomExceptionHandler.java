package com.example.common.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.dto.response.APIResponse;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception e, WebRequest request) {
		APIResponse<String> result = new APIResponse<String>();
		result.setError(e.getMessage());
		result.setStatus(APIStatus.BAD_REQUEST);
		return ResponseHelper.setFailedResult(result, APIStatus.BAD_REQUEST.getCode(), null);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomExceptions(CustomException e, WebRequest request) {
		APIResponse<String> result = new APIResponse<String>();
		result.setError(e.getMessage());
		result.setStatus(e.getStatus());
		return ResponseHelper.setFailedResult(result, e.getStatus().getCode(), null);
	}	
	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException e,
			HttpHeaders headers, HttpStatus status, final WebRequest request) {
		APIResponse<String> result = new APIResponse<String>();
		result.setError(e.getMessage());
		result.setStatus(APIStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(result, status);
	}
}
