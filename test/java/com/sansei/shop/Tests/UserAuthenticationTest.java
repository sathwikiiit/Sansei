package com.sansei.shop.Tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.repository.ApiUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApiUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setup() {
        ApiUser testuser = new ApiUser("testuser", passwordEncoder.encode("testpassword"), "testemail", "1234", "51122" );
        testuser.setRole("user");
        userRepository.save(testuser);
        ApiUser adminuser = new ApiUser("adminuser", passwordEncoder.encode("testpassword"), "testemail", "1234", "51122" );
        adminuser.setRole("admin");
        userRepository.save(adminuser);
    }
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testUserAuthentication() throws Exception {
        String token = mockMvc.perform (post("/login").param("username", "testuser")
        .param("password", "testpassword")
        )
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        assertNotNull(token);
        mockMvc.perform( get("/user").header("Authorization", "Bearer "+ token)).andExpect( status().isOk());
    }

    @Test
    public void testAdminAuthentication() throws Exception {
        String token = mockMvc.perform (post("/login").param("username", "adminuser")
                .param("password", "testpassword")
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(token);
        mockMvc.perform( get("/user").header("Authorization", "Bearer "+ token)).andExpect( status().isOk());
    }
}