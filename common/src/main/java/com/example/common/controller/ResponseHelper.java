package com.example.common.controller;

import org.springframework.http.ResponseEntity;

import com.example.common.dto.response.APIResponse;

public class ResponseHelper {
	public static <S> ResponseEntity<APIResponse<S>> setSuccessResult(APIResponse<S> response,String methodType) {
		return ResponseEntity.ok().header("Access-Control-Allow-Methods",methodType).header("Access-Control-Allow-Headers", "Content-Type").body(response);		       
	}
	
	public static <S>  ResponseEntity<APIResponse<S>> setFailedResult(APIResponse<S> response, int code,String methodType) {		
		return ResponseEntity.status(code).header("Access-Control-Allow-Methods",methodType).header("Access-Control-Allow-Headers", "Content-Type").body(response);		       
	}
}

