package com.sansei.shop.Tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.sansei.shop.model.Product;
import com.sansei.shop.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        productRepository.save(new Product("Timex Watch", 500.0, "Analog watch with leather strap", "Electronics", "watch,timex,analog,latest,premium", 255));
        productRepository.save( new Product("Apple Watch", 1000.0, "Smart watch with digital display", "Electronics", "watch,apple,smart,digital,latest", 255));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray()) // Check if the response is an array
            .andExpect(jsonPath("$[0].name").value("Timex Watch")) // Check the first product's name
            .andExpect(jsonPath("$[0].price").value(500.0)) // Check the first product's price
            .andExpect(jsonPath("$[1].name").value("Apple Watch")) // Check the second product's name
            .andExpect(jsonPath("$[1].price").value(1000.0)); // Check the second product's price
    }

    @Test
    public void testGetProductbyCategoryFilters() throws Exception {
        mockMvc.perform(get("/products/filter?category=Electronics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2));
    }
    @Test
    public void testGetProductbyPriceFilters() throws Exception {
        mockMvc.perform(get("/products/filter?searchString=apple"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1));
    }
}
