package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private String modifiedBy;
    private LocalDateTime modifiedOn;

    // getters/setters
}


///eplaination of what and why we use when building large apps

// We avoid @Data in JPA entities in large applications because it automatically
// generates equals(), hashCode(), and toString(). These methods can unintentionally
// include entity relationships, causing circular references, unnecessary lazy-loading,
// or performance issues. Using @Getter and @Setter gives us more control over
// which methods are generated.
