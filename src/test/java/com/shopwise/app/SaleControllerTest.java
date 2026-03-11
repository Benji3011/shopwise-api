package com.shopwise.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.shopwise.app.dto.response.SaleItemResponse;
import com.shopwise.app.dto.response.SaleResponse;
import com.shopwise.app.service.SaleService;

@SpringBootTest
class SaleControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private SaleService saleService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    private SaleResponse fakeSale() {
        SaleItemResponse item = new SaleItemResponse();
        item.setId(1L);
        item.setProductId(1L);
        item.setProductName("iPhone 15");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("999.99"));

        SaleResponse sale = new SaleResponse();
        sale.setId(1L);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setItems(List.of(item));
        sale.setTotal(new BigDecimal("1999.98"));
        return sale;
    }

    @Test
    void createSale_admin_ok() throws Exception {
        when(saleService.create(any())).thenReturn(fakeSale());

        String body = """
            {"items":[{"productId":1,"quantity":2}]}
            """;

        mockMvc.perform(post("/api/sales")
                .with(httpBasic("admin", "shopwise123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.total").value(1999.98));
    }

    @Test
    void createSale_user_forbidden() throws Exception {
        String body = """
            {"items":[{"productId":1,"quantity":1}]}
            """;

        mockMvc.perform(post("/api/sales")
                .with(httpBasic("user", "user123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    void createSale_emptyItems_badRequest() throws Exception {
        mockMvc.perform(post("/api/sales")
                .with(httpBasic("admin", "shopwise123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"items\":[]}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getSale_user_ok() throws Exception {
        when(saleService.getById(1L)).thenReturn(fakeSale());

        mockMvc.perform(get("/api/sales/1")
                .with(httpBasic("user", "user123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getSales_unauthenticated_401() throws Exception {
        mockMvc.perform(get("/api/sales"))
            .andExpect(status().isUnauthorized());
    }
}
