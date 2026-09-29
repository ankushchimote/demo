package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSaveProduct() {

        // Arrange
        ProductRequest request = new ProductRequest();
        request.setProductName("iPhone");
        request.setCreatedBy("rahul");

        Product savedProduct = new Product();
        savedProduct.setProductName("iPhone");
        savedProduct.setCreatedBy("rahul");

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(request);

        // Assert
        assertEquals("iPhone", result.getProductName());
        assertEquals("rahul", result.getCreatedBy());

        verify(productRepository, times(1))
                .save(any(Product.class));
    }
    @Test
    void getProductById_shouldReturnProduct() {

        // Arrange
        Integer productId = 1;

        Product product = new Product();
        product.setId(productId);
        product.setProductName("iPhone");
        product.setCreatedBy("rahul");

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertEquals(productId, result.getId());
        assertEquals("iPhone", result.getProductName());
        assertEquals("rahul", result.getCreatedBy());

        verify(productRepository, times(1))
                .findById(productId);
    }

}

