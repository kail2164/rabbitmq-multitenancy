package com.example.product.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.common.constants.RabbitMQConstants;
import com.example.common.constants.TestConstants;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.LoginResponse;
import com.example.common.dto.response.ProductResponse;
import com.example.common.util.JsonUtils;
import com.example.common.util.RabbitMQUtils;
import com.example.product.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ProductControllerTest {
	private String AUTHORIZATION_HEADER = "Authorization";
	private String authorizationValue;
	private ProductResponse response;
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@Autowired
	RabbitMQUtils rabbitMQUtils;

	@BeforeAll
	void setup() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setUsername("test");
		request.setPassword("123456789");
		LoginResponse loginResponse = rabbitMQUtils.sendAndReceive(RabbitMQConstants.TOPIC_ACCOUNT,
				RabbitMQConstants.ROUTING_ACCOUNT_LOGIN, request, LoginResponse.class);
		authorizationValue = "Bearer " + loginResponse.getToken();
		response = new ProductResponse();
		response.setId(TestConstants.ID);
		response.setImgUrl(TestConstants.PASSED);
		response.setName(TestConstants.PASSED);
		response.setPrice(10d);
	}

	@Test
	void testGetProduct_FAIL_NoAuthorizationHeader() throws Exception {
		String uri = "/api/product/";
		mvc.perform(get(uri)).andExpect(status().is(401)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Missing Authorization header")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testGetProduct_FAIL_NoParam() throws Exception {
		String uri = "/api/product/";
		mvc.perform(get(uri).header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request parameter")))
				.andExpect(jsonPath("$.success", is(false)));

		mvc.perform(get(uri).param("page", "1").header(AUTHORIZATION_HEADER, authorizationValue))
				.andExpect(status().is(400)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request parameter")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testGetProduct_SUCCESS() throws Exception {
		List<ProductResponse> listProducts = new ArrayList<>();
		listProducts.add(response);
		ProductResponse response2 = new ProductResponse();
		response2.setId(2l);
		response2.setImgUrl(TestConstants.PASSED);
		response2.setName(TestConstants.PASSED);
		response2.setPrice(20d);
		listProducts.add(response2);
		doReturn(listProducts).when(productService).getProducts(anyInt(), anyInt());
		String uri = "/api/product/";
		mvc.perform(
				get(uri).param("page", "1").param("maxRecords", "5").header(AUTHORIZATION_HEADER, authorizationValue))
				.andExpect(status().isOk()).andExpect(jsonPath("$.result.length()", is(2)))
				.andExpect(jsonPath("$.result[0].id", is(1)))
				.andExpect(jsonPath("$.result[0].imgUrl", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result[0].name", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result[0].price", is(10d))).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

	@Test
	void testCreateProduct_FAIL_NoAuthorizationHeader() throws Exception {
		String uri = "/api/product/";
		mvc.perform(post(uri)).andExpect(status().is(401)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Missing Authorization header")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testCreateProduct_FAIL_NoBody() throws Exception {
		String uri = "/api/product/";
		mvc.perform(post(uri).header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request body")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testCreateProduct_SUCCESS() throws Exception {
		doReturn(response).when(productService).create(any(ProductRequest.class));
		String uri = "/api/product/";
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new ProductRequest()))
				.header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.imgUrl", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.name", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.price", is(10d))).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

	@Test
	void testUpdateProduct_FAIL_NoAuthorizationHeader() throws Exception {
		String uri = "/api/product/";
		mvc.perform(put(uri)).andExpect(status().is(401)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Missing Authorization header")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testUpdateProduct_FAIL_NoBodyAndParam() throws Exception {
		String uri = "/api/product/";
		mvc.perform(put(uri).header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request param")))
				.andExpect(jsonPath("$.success", is(false)));

		mvc.perform(put(uri).param("nullableUpdate", "false").header(AUTHORIZATION_HEADER, authorizationValue))
				.andExpect(status().is(400)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request body")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testUpdateProduct_SUCCESS() throws Exception {
		doReturn(response).when(productService).update(any(ProductRequest.class), anyBoolean());
		String uri = "/api/product/";
		mvc.perform(put(uri).header(AUTHORIZATION_HEADER, authorizationValue).param("nullableUpdate", "true")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(new ProductRequest()))
				.header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.imgUrl", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.name", is(TestConstants.PASSED)))
				.andExpect(jsonPath("$.result.price", is(10d))).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}

	@Test
	void testDeleteProduct_FAIL_NoAuthorizationHeader() throws Exception {
		String uri = "/api/product/";
		mvc.perform(delete(uri)).andExpect(status().is(401)).andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Missing Authorization header")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testDeleteProduct_FAIL_NoParam() throws Exception {
		String uri = "/api/product/";
		mvc.perform(delete(uri).header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().is(400))
				.andExpect(jsonPath("$.result", Matchers.nullValue()))
				.andExpect(jsonPath("$.error", Matchers.containsString("Required request param")))
				.andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	void testDeleteProduct_SUCCESS() throws Exception {
		Long responseLong = 1l;
		doReturn(responseLong).when(productService).delete(anyLong());
		String uri = "/api/product/";
		mvc.perform(delete(uri).header(AUTHORIZATION_HEADER, authorizationValue).param("id", "1")
				.header(AUTHORIZATION_HEADER, authorizationValue)).andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(1))).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.error", Matchers.nullValue()));
	}
}
