package com.sansei.shop.Tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.model.Product;
import com.sansei.shop.repository.ApiUserRepository;
import com.sansei.shop.repository.ProductRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserOrderTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApiUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        ApiUser testuser = new ApiUser("testuser", passwordEncoder.encode("testpassword"), "testemail", "1234",
                "51122");
        testuser.setRole("user");
        userRepository.save(testuser);
        productRepository.deleteAll();
        productRepository.save(new Product("Timex Watch", 500.0, "Analog watch with leather strap",
                "Electronics", "watch,timex,analog,latest,premium", 255));
        productRepository.save(new Product("Apple Watch", 1000.0, "Smart watch with digital display",
                "Electronics", "watch,apple,smart,digital,latest", 255));

    }

    @Test
    public void testUserOrder() throws Exception {
        String token = mockMvc.perform(post("/login").param("username", "testuser")
                .param("password", "testpassword")).andReturn().getResponse().getContentAsString();

        List<Product> sampleProducts = productRepository.findAll().subList(0, 2);
        mockMvc.perform(get("/user/cart").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        // Adding the first product to the cart
        mockMvc.perform(put("/user/cart/add/" + String.valueOf(sampleProducts.get(0).getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
                .equals(" Product added to cart");
        // Adding the second product to the cart
        mockMvc.perform(put("/user/cart/add/" + String.valueOf(sampleProducts.get(1).getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
                .equals(" Product added to cart");
        // Removing the second product from the cart
        mockMvc.perform(put("/user/cart/remove/" + String.valueOf(sampleProducts.get(1).getId()))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
                .equals("Product removed from cart");
        // Placing an order
        mockMvc.perform(post("/user/order")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
                .equals("Order placed");
        // Retrieving user orders
        mockMvc.perform(get("/user/orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
