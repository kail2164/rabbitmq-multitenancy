package com.example.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.controller.ResponseHelper;
import com.example.common.controller.SecuredController;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.APIResponse;
import com.example.common.dto.response.ProductResponse;
import com.example.common.dto.swagger.document.LongDocument;
import com.example.common.dto.swagger.document.product.ListProductDocument;
import com.example.common.dto.swagger.document.product.ProductDocument;
import com.example.common.dto.swagger.document.response.ResponseError400;
import com.example.common.dto.swagger.document.response.ResponseError500;
import com.example.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product Controller", description = "Controller for actions related to product")
public class ProductController implements SecuredController {
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping()
	@Operation(description = "Get products")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ListProductDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> getProducts(HttpServletRequest request,
			@Parameter(allowEmptyValue = false, description = "Page number, starts from 1", example = "1") @RequestParam(required = true) int page,
			@Parameter(allowEmptyValue = false, description = "Records per page", example = "20") @RequestParam(required = true) int maxRecords)
			throws Exception {
		return ResponseHelper.setSuccessResult(
				new APIResponse<List<ProductResponse>>(productService.getProducts(page, maxRecords)),
				request.getMethod());
	}

	@PostMapping()
	@Operation(description = "Create product")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ListProductDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> createProduct(HttpServletRequest request,
			@Parameter(name = "ProductRequestDTO", description = "c request object", required = true, allowEmptyValue = false) @RequestBody(required = true) ProductRequest product)
			throws Exception {
		return ResponseHelper.setSuccessResult(new APIResponse<ProductResponse>(productService.create(product)),
				request.getMethod());
	}

	@PutMapping()
	@Operation(description = "Update product")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ProductDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> updateProduct(HttpServletRequest request,
			@Parameter(allowEmptyValue = false, description = "Is able to update with null values", example = "false") @RequestParam(required = true) boolean nullableUpdate,
			@Parameter(name = "ProductRequestDTO", description = "Product request object", required = true, allowEmptyValue = false) @RequestBody(required = true) ProductRequest product)
			throws Exception {
		return ResponseHelper.setSuccessResult(
				new APIResponse<ProductResponse>(productService.update(product, nullableUpdate)), request.getMethod());
	}

	@DeleteMapping()
	@Operation(description = "Delete product")
	@ApiResponse(content = @Content(schema = @Schema(implementation = LongDocument.class), mediaType = "application/json"), responseCode = "200", description = "OK")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError400.class), mediaType = "application/json"), responseCode = "400", description = "Bad Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = ResponseError500.class), mediaType = "application/json"), responseCode = "500", description = "Internal Server Error")
	public ResponseEntity<?> deleteProduct(HttpServletRequest request,
			@Parameter(allowEmptyValue = false, description = "ID to delete", example = "1") @RequestParam(required = true) Long id)
			throws Exception {
		return ResponseHelper.setSuccessResult(new APIResponse<Long>(productService.delete(id)), request.getMethod());
	}
}
