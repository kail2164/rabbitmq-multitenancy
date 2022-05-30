package com.example.product.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.common.constants.TestConstants;
import com.example.common.dto.CustomException;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;
import com.example.product.converter.ProductConverter;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;

@SpringBootTest
class ProductServiceImplTest {	
	
	@InjectMocks
	private ProductService productService = new ProductServiceImpl();	

	@Mock
	private ProductRepository productRepository;
	

	@Test
	void testGetProducts_SUCCESS_EmptyList() {
		List<Product> empty = new ArrayList<>();
		Page<Product> products = new PageImpl<>(empty);
		doReturn(products).when(productRepository).findAll(any(Pageable.class));
		List<ProductResponse> results = assertDoesNotThrow(() -> productService.getProducts(1, 10));
		assertTrue(results.size() == 0);
	}
	
	@Test
	void testGetProducts_SUCCESS_NotEmptyList() {
		List<Product> listProduct = new ArrayList<>();
		Product product = new Product();
		product.setName("Test1");
		listProduct.add(product);
		product = new Product();
		product.setName("Test2");
		listProduct.add(product);
		Page<Product> products = new PageImpl<>(listProduct);
		doReturn(products).when(productRepository).findAll(any(Pageable.class));
		List<ProductResponse> results = assertDoesNotThrow(() -> productService.getProducts(1, 10));
		assertTrue(results.size() == 2);
	}

	@Test
	void testCreate_FAIL_NullImgUrl() {
		ProductRequest request = new ProductRequest();
		Exception ex = assertThrows(CustomException.class, () -> productService.create(request));
		String expectedMessage = "Image URL can't be null or empty";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testCreate_FAIL_NullName() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> productService.create(request));
		String expectedMessage = "Product's name can't be null or empty";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testCreate_FAIL_InvalidImgUrlLength() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.INVALID_MIN_LENGTH_STR);
		request.setName(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> productService.create(request));
		String expectedMessage = "Image URL length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
		
		ProductRequest request2 = new ProductRequest();
		request2.setImgUrl(TestConstants.INVALID_MAX_LENGTH_STR);
		request2.setName(TestConstants.PASSED);
		ex = assertThrows(CustomException.class, () -> productService.create(request2));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testCreate_FAIL_InvalidNameLength() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.INVALID_MIN_LENGTH_STR);
		Exception ex = assertThrows(CustomException.class, () -> productService.create(request));
		String expectedMessage = "Product's name length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
		
		ProductRequest request2 = new ProductRequest();
		request2.setImgUrl(TestConstants.PASSED);
		request2.setName(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> productService.create(request2));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testCreate_FAIL_ExceptionThrewWhenSaving() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		doThrow(IllegalArgumentException.class).when(productRepository).save(any(Product.class));
		assertThrows(CustomException.class, () -> productService.create(request));
	}

	@Test
	void testCreate_SUCCESS() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		request.setPrice(10.0);
		Product product = ProductConverter.convertFromRequest(request);
		doReturn(product).when(productRepository).save(any(Product.class));
		ProductResponse result = assertDoesNotThrow(() -> productService.create(request));
		assertEquals(TestConstants.PASSED, result.getImgUrl());
		assertEquals(TestConstants.PASSED, result.getName());
		assertEquals(10.0, result.getPrice());
	}
	
	@Test
	void testUpdate_FAIL_InvalidImgUrlLength() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.INVALID_MIN_LENGTH_STR);
		request.setName(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> productService.update(request, false));
		String expectedMessage = "Image URL length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
		
		ProductRequest request2 = new ProductRequest();
		request2.setImgUrl(TestConstants.INVALID_MAX_LENGTH_STR);
		request2.setName(TestConstants.PASSED);
		ex = assertThrows(CustomException.class, () -> productService.update(request2, false));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testUpdate_FAIL_InvalidNameLength() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.INVALID_MIN_LENGTH_STR);
		Exception ex = assertThrows(CustomException.class, () -> productService.update(request, false));
		String expectedMessage = "Product's name length is from 2-255 characters";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
		
		ProductRequest request2 = new ProductRequest();
		request2.setImgUrl(TestConstants.PASSED);
		request2.setName(TestConstants.INVALID_MAX_LENGTH_STR);
		ex = assertThrows(CustomException.class, () -> productService.update(request2, false));
		actualMessage = ex.getMessage();
		assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testUpdate_FAIL_NullId() {
		ProductRequest request = new ProductRequest();
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		Exception ex = assertThrows(CustomException.class, () -> productService.update(request, false));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	

	@Test
	void testUpdate_FAIL_NullProduct() {
		ProductRequest request = new ProductRequest();
		request.setId(1l);
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		Product product = null;
		doReturn(product).when(productRepository).getById(any(Long.class));
		Exception ex = assertThrows(CustomException.class, () -> productService.update(request, false));
		String expectedMessage = "Not found";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testUpdate_FAIL_ExceptionThrewWhenSaving() {
		ProductRequest request = new ProductRequest();
		request.setId(1l);
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		Product gotProduct = ProductConverter.convertFromRequest(request);
		doReturn(gotProduct).when(productRepository).getById(any(Long.class));
		doThrow(IllegalArgumentException.class).when(productRepository).save(any(Product.class));
		assertThrows(CustomException.class, () -> productService.update(request, false));
	}

	@Test
	void testUpdate_SUCCESS_NullableUpdateIsFalse() {
		ProductRequest request = new ProductRequest();
		request.setId(1l);
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		request.setPrice(10.0);
		Product gotProduct = ProductConverter.convertFromRequest(request);
		request.setPrice(20.0);
		Product savedProduct = ProductConverter.convertFromRequest(request);
		request.setName(null);
		doReturn(savedProduct).when(productRepository).save(any(Product.class));
		doReturn(gotProduct).when(productRepository).getById(any(Long.class));
		ProductResponse result = assertDoesNotThrow(() -> productService.update(request, false));
		assertEquals(TestConstants.PASSED, result.getImgUrl());
		assertEquals(TestConstants.PASSED, result.getName());
		assertEquals(20.0, result.getPrice());
	}
	
	@Test
	void testUpdate_SUCCESS_NullableUpdateIsTrue() {
		ProductRequest request = new ProductRequest();
		request.setId(1l);
		request.setImgUrl(TestConstants.PASSED);
		request.setName(TestConstants.PASSED);
		request.setPrice(10.0);
		Product gotProduct = ProductConverter.convertFromRequest(request);
		request.setName(null);		request.setImgUrl(null);

		request.setPrice(20.0);
		Product savedProduct = ProductConverter.convertFromRequest(request);
		doReturn(savedProduct).when(productRepository).save(any(Product.class));
		doReturn(gotProduct).when(productRepository).getById(any(Long.class));
		ProductResponse result = assertDoesNotThrow(() -> productService.update(request, true));
		assertNull(result.getImgUrl());
		assertNull(result.getName());
		assertEquals(20.0, result.getPrice());
	}
	@Test
	void testDelete_FAIL_NullId() {
		Long id = null;
		Exception ex = assertThrows(CustomException.class, () -> productService.delete(id));
		String expectedMessage = "cannot be null";
		String actualMessage = ex.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete_SUCCESS() {
		Long id = 1l;
		Long result = assertDoesNotThrow(() -> productService.delete(id));		
		assertEquals(1l, result);
	}

}
