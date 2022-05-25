package com.example.product.controller;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.common.constants.TestConstants;
import com.example.common.dto.response.ProductResponse;
import com.example.product.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;
	
//	@Test
//	@WithMockUser(authorities = { "ROLE_ADMIN" })
//	void testGetProduct_FAIL_NoParam() throws Exception {		
//		String uri = "/api/product/";
//		mvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().is(400))
//				.andExpect(jsonPath("$.result", Matchers.nullValue()))
//				.andExpect(jsonPath("$.error", containsString("required request parameter")))
//				.andExpect(jsonPath("$.success", is(false)));
//		
//		mvc.perform(get(uri).param("page", "1").contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().is(400))
//		.andExpect(jsonPath("$.result", Matchers.nullValue()))
//		.andExpect(jsonPath("$.error", containsString("required request parameter")))
//		.andExpect(jsonPath("$.success", is(false)));
//	}	

	@Test
	@WithMockUser(authorities = { "ROLE_ADMIN" })
	void testGetProduct_SUCCESS() throws Exception {
		List<ProductResponse> listProducts = new ArrayList<>();
		ProductResponse response = new ProductResponse();
		response.setId(1l);
		response.setImgUrl(TestConstants.PASSED);
		response.setName(TestConstants.PASSED);
		response.setPrice(10d);
		listProducts.add(response);
		response = new ProductResponse();
		response.setId(2l);
		response.setImgUrl(TestConstants.PASSED);
		response.setName(TestConstants.PASSED);
		response.setPrice(20d);
		listProducts.add(response);
		doReturn(listProducts).when(productService).getProducts(anyInt(), anyInt());
		String uri = "/api/product/";
		mvc.perform(
				get(uri)
				.param("page", "1")
				.param("maxRecords", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.length()", is(2)))
				.andExpect(jsonPath("$.result[0].id", is(1)))
				.andExpect(jsonPath("$.result[0].imgUrl", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result[0].name", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result[0].price", is(10d)))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

//	@Test
//	void testCreateProduct() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	void testUpdateProductHttpServletRequestBooleanProductRequest() {
//		fail("Not yet implemented"); // TODO
//	}

}
