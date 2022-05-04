package com.example.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.controller.ResponseHelper;
import com.example.common.controller.SecuredController;
import com.example.common.dto.response.APIResponse;
import com.example.common.dto.response.ProductResponse;
import com.example.common.dto.swagger.document.product.ListProductDocument;
import com.example.common.dto.swagger.document.response.ResponseError400;
import com.example.common.dto.swagger.document.response.ResponseError500;
import com.example.product.publisher.AccountPublisher;
import com.example.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Campaign Controller", description = "Controller for actions related to product")
public class ProductController implements SecuredController {
	@Autowired
	ProductService productService;
	@Autowired
	AccountPublisher test;

	@GetMapping()
	@Operation(description = "Get products ")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ListProductDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
//	public ResponseEntity<?> getProducts(HttpServletRequest request,
//			@Parameter(allowEmptyValue = false, description = "Page number, starts from 1", example = "1") @RequestParam(required = true) int page,
//			@Parameter(allowEmptyValue = false, description = "Records per page", example = "20") @RequestParam(required = true) int maxRecords))
	public ResponseEntity<?> getProducts(HttpServletRequest request) throws Exception {
		test.fetchAllSchemas();
		return ResponseHelper.setSuccessResult(new APIResponse<List<ProductResponse>>(
				productService.getProducts()), request.getMethod());
	}
}
