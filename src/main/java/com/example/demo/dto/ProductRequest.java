package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    @NotBlank(message = "Created by is required")
    @Size(max = 100, message = "Created by must not exceed 100 characters")
    private String createdBy;
}


// Request DTO is used to validate and control client input before it reaches the service layer.
// Jakarta Bean Validation annotations such as @NotBlank and @Size ensure required fields
// are present and within the expected limits. @Valid in the controller triggers these validations.

//or
// Validates incoming product data before it reaches the service layer.