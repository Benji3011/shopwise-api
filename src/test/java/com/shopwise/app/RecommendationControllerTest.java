package com.shopwise.app;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.shopwise.app.dto.response.RecommendationResponse;
import com.shopwise.app.service.RecommendationService;

@SpringBootTest
class RecommendationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private RecommendationService recommendationService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void topProducts_user_ok() throws Exception {
        when(recommendationService.getTopProducts(5)).thenReturn(List.of(
            new RecommendationResponse(1L, "iPhone 15", new BigDecimal("999.99"), 10L),
            new RecommendationResponse(2L, "Samsung S24", new BigDecimal("899.99"), 7L)
        ));

        mockMvc.perform(get("/api/recommendations/top?limit=5")
                .with(httpBasic("user", "user123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("iPhone 15"))
            .andExpect(jsonPath("$[0].score").value(10));
    }

    @Test
    void similarProducts_user_ok() throws Exception {
        when(recommendationService.getSimilarProducts(1L, 5)).thenReturn(List.of(
            new RecommendationResponse(5L, "AirPods Pro", new BigDecimal("249.99"), 3L)
        ));

        mockMvc.perform(get("/api/recommendations/similar/1?limit=5")
                .with(httpBasic("user", "user123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].productName").value("AirPods Pro"));
    }

    @Test
    void recommendations_unauthenticated_401() throws Exception {
        mockMvc.perform(get("/api/recommendations/top"))
            .andExpect(status().isUnauthorized());
    }
}
