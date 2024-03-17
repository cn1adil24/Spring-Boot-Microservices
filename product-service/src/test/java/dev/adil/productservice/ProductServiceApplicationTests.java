package dev.adil.productservice;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.adil.productservice.ddo.ProductRequest;
import dev.adil.productservice.ddo.ProductResponse;
import dev.adil.productservice.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.6");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;
	
	static {
        mongoDBContainer.start();
    }
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	
	@Test
	void shouldAddProduct() throws Exception {
		addProduct();
		
		Assertions.assertEquals(1, productRepository.findAll().size());
	}
	
	@Test
	void shouldGetAllProducts() throws Exception {
		//addProduct();
		
		List<ProductResponse> productResponses = getProducts();
		
		Assertions.assertEquals(1, productResponses.size());
	    //Assertions.assertTrue(productResponses.stream().anyMatch(productResponse -> productResponse.getName().equals("Iphone 13")));
	    Assertions.assertEquals("Iphone 13", productResponses.get(0).getName());
	    Assertions.assertEquals("Iphone 13 from Apple", productResponses.get(0).getDescription());
	    Assertions.assertEquals(BigDecimal.valueOf(1300), productResponses.get(0).getPrice());
	}
	
	private void addProduct() throws Exception {
		ProductRequest product = getProductRequest();
		String productStr = objectMapper.writeValueAsString(product);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
											.contentType(MediaType.APPLICATION_JSON)
											.content(productStr)
											)
			.andExpect(status().isCreated());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("Iphone 13 from Apple")
				.price(BigDecimal.valueOf(1300))
				.build();
	}
	
	private List<ProductResponse> getProducts() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
			.andExpect(status().isOk())
			.andReturn();
		
		List<ProductResponse> productResponses = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<List<ProductResponse>>() {}
				);
		return productResponses;
	}
}
