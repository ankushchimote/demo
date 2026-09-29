package com.example.demo.controller;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Item;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@SecurityRequirement(name = "bearerAuth")// it only tells swagger to Send the Bearer token when calling this endpoint.
@RequiredArgsConstructor  /// when using this annotation we dont need to write constrctor's bolier plate which i commented.same thing is done in productservice.
public class ProductController {

    private final ProductService productService;

//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts(){
//        return ResponseEntity.ok(productService.getAllProducts());
//    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
         return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest productRequest){
        Product createProduct=productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,Product product){
        return ResponseEntity.ok(productService.updateProduct(id,product));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
        //ResponseEntity.noContent().build() returns an HTTP 204 No Content,
        // which is the standard RESTful response for a successful DELETE operation
        // where no body needs to be returned.
    }
    @GetMapping("/{id}/items")
    public ResponseEntity<List<Item>> getItemsByProductId(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getItemsByProductId(id));
    }

}


/*
 * Constructor Injection is preferred over Field Injection (@Autowired) because:
 * 1. IMMUTABILITY: Dependencies can be declared 'final', preventing accidental modification after initialization.
 * 2. EASE OF TESTING: Allows easy unit testing by instantiating the class with standard constructor calls without needing Spring Context or reflection.
 * 3. EXPLICIT CONTRACTS: Prevents runtime NullPointerExceptions by ensuring all required dependencies are provided at compile-time/object instantiation.
 * 4. CIRCULAR DEPENDENCY DETECTION: Fails fast during application startup if circular dependencies exist.
 */

//PR Comment Suggestion:
//"Prefer Constructor Injection over @Autowired on fields.
// Constructor injection allows dependencies to be final (ensuring immutability) and
// makes unit testing much easier since dependencies can be passed manually without
// booting up a Spring Test context."


///for pagination testing swagger will give u
//page     0
//size     20
//        sort     id,asc