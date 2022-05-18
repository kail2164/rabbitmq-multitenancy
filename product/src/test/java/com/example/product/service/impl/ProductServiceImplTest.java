package com.example.product.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.common.dto.response.ProductResponse;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {	
	
	@InjectMocks
	private ProductService productService = new ProductServiceImpl();	

	@Mock
	private ProductRepository productRepository;
	

	@Test
	void testGetProducts_SUCCESS_EmptyList() {
		Page<Product> products = Mockito.mock(Page.class);
		doReturn(products).when(productRepository).findAll(any(Pageable.class));
		List<ProductResponse> results = assertDoesNotThrow(() -> productService.getProducts(1, 10));
		assertTrue(results.size() == 0);
	}

	@Test
	void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testDelete() {
		fail("Not yet implemented"); // TODO
	}

}
