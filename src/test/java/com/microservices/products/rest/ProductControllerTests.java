package com.microservices.products.rest;

import com.microservices.products.entities.Product;
import com.microservices.products.repositories.ProductRepository;
import com.microservices.products.services.ProductService;
import com.microservices.products.utils.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
public class ProductControllerTests {
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    void prepare() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser
    void whenProductDeleted_thenDeleteProductInvokedOnce() throws Exception {
        mockMvc.perform(delete("/v1/product/1"))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @WithMockUser
    void whenProductAdded_thenReturnHttpStatusCreated() throws Exception {
        Product product = productBuilder("Arduino Uno", "Electronics", Product.ProductType.ELECTRONIC, 5, new BigDecimal("3500.00"));
        String jsonString = JSONUtils.asJsonString(product);

        when(productService.addProduct(product))
                .thenReturn(product);

        mockMvc.perform(post("/v1/product/")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        verify(productService, times(1)).addProduct(product);
    }

    @Test
    @WithMockUser
    void whenProductUpdatedWithValidData_thenReturnStatusOK() throws Exception {
        Product product = productBuilder("Arduino Uno", "Electronics", Product.ProductType.ELECTRONIC, 5, new BigDecimal("3500.00"));

        when(productService.updateProduct(1L, product))
                .thenReturn(product);

        mockMvc.perform(put("/v1/product/1")
                .content(JSONUtils.asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(1L, product);
    }

    @Test
    @WithMockUser
    void whenProductUpdatedWithInvalidData_thenReturnStatusHttpBadRequest() throws Exception {
        Product product = productBuilder("Arduino Uno", "Electronics", Product.ProductType.ELECTRONIC, -1, new BigDecimal("-3500.00"));

        when(productService.updateProduct(1L, product))
                .thenReturn(null);

        mockMvc.perform(put("/v1/product/1")
                .content(JSONUtils.asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());

        verify(productService, times(0)).updateProduct(1L, product);
    }

    @Test
    @WithMockUser
    void whenProductRetrieved_thenReturnProduct() throws Exception {
        Product p = productBuilder("Arduino Uno", "Electronics", Product.ProductType.ELECTRONIC, 5, new BigDecimal("3500.00"));
        when(productService.findById(1L)).thenReturn(p);

        mockMvc.perform(get("/v1/product/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.productType").exists())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.price").exists());

        verify(productService, times(1)).findById(1L);
    }



    private Product productBuilder(String name, String description, Product.ProductType type, Integer quantity, BigDecimal price) {
        Product result = new Product();

        result.setName(name);
        result.setDescription(description);
        result.setProductType(type);
        result.setQuantity(quantity);
        result.setPrice(price);

        return result;
    }
}
