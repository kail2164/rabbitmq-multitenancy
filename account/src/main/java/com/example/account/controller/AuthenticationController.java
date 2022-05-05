package com.example.account.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.account.service.AuthenticationService;
import com.example.common.controller.ResponseHelper;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.RegisterRequest;
import com.example.common.dto.response.APIResponse;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.RegisterResponse;
import com.example.common.dto.swagger.document.account.LoginDocument;
import com.example.common.dto.swagger.document.account.RegisterDocument;
import com.example.common.dto.swagger.document.response.ResponseError400;
import com.example.common.dto.swagger.document.response.ResponseError500;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Controller for actions related to authentication")
@NoArgsConstructor
public class AuthenticationController {
	private AuthenticationService authenticationService;
	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	@Operation(description = "API used for logging in")
	@ApiResponse(content = @Content(schema = @Schema(implementation = LoginDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request,
			@Parameter(name = "LoginRequestDTO", description = "Login request object", required = true, allowEmptyValue = false) @RequestBody LoginRequest loginRequest)
			throws CustomException {
		return ResponseHelper.setSuccessResult(
				new APIResponse<LoginResponse>(authenticationService.login(loginRequest)), request.getMethod());
	}

	@GetMapping("/logout")
	@Operation(description = "API used for logging out")
	@ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<String> logout(@RequestParam(name = "token", required = true) String token)
			throws CustomException {
		authenticationService.logout(token);
		return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
	}

	@PostMapping("/register")
	@Operation(description = "API used for registering")
	@ApiResponse(content = @Content(schema = @Schema(implementation = RegisterDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> createAccount(HttpServletRequest request,
			@Parameter(name = "RegisterRequestDTO", description = "Register Request Object", required = true, allowEmptyValue = false) @RequestBody RegisterRequest registerRequest)
			throws CustomException {
		return ResponseHelper.setSuccessResult(
				new APIResponse<RegisterResponse>(authenticationService.register(registerRequest)),
				request.getMethod());
	}

}
