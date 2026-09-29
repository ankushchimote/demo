package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Item;
import com.example.demo.model.Product;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

//    public List<Product> getAllProducts(){
//        return productRepository.findAll();
//    }
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Integer id){
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(ProductRequest productRequest) {

        Product product = new Product();

        product.setProductName(productRequest.getProductName());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCreatedOn(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id,Product product){
        Product existingProduct=productRepository.findById(id)
                .orElseThrow(()->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));
        existingProduct.setProductName(product.getProductName());
        existingProduct.setModifiedBy(product.getModifiedBy());
        existingProduct.setModifiedOn(product.getModifiedOn());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Integer id){
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        //System.out.println("product with id:"+ id+ "deleted");
        log.info("Product with id: {} deleted", id);
        //Replacing System.out.println with Spring’s standard logger (like LoggerFactory or Lombok's @Slf4j) is recommended for production applications:
    }
    public List<Item> getItemsByProductId(Integer productId){
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(
                    "Product not found with id: " + productId);
        }

        return itemRepository.findByProductId(productId);
    }

}

//Why don't we need ItemService yet?
//Your only Item-related endpoint is:GET /api/v1/products/{id}/items
//The operation is really:
//        "Give me the items belonging to this product."
//
//That's still a Product resource operation, so ProductService can handle it:



///exception handling explaination
// The Service layer is responsible for detecting business errors and throwing
// meaningful exceptions such as ProductNotFoundException. The Global Exception
// Handler catches these exceptions centrally and converts them into appropriate
// HTTP responses (e.g., 404 NOT_FOUND), avoiding repetitive try-catch blocks
// across multiple controllers.


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





//api testing format of data:
//update
//{
//    "productName": "iPhone 17",
//        "modifiedBy": "Rahul",
//        "modifiedOn": "2026-09-24T19:00:00"
//}


//create
//{
//    "productName": "iPhone 17",
//        "modifiedBy": "Rahul"
//}





