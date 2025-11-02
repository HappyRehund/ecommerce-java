package com.rehund.ecommerce.controller;

import com.rehund.ecommerce.model.ProductRequest;
import com.rehund.ecommerce.model.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name("product" + productId)
                        .price(BigDecimal.ONE)
                        .description("a")
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(
                List.of(
                        ProductResponse.builder()
                                .name("product 1")
                                .description("ab")
                                .price(BigDecimal.ONE)
                                .build(),
                        ProductResponse.builder()
                                .name("product 2")
                                .description("ab")
                                .price(BigDecimal.ONE)
                                .build()
                )
        );
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid  ProductRequest request){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody @Valid ProductRequest request,
            @PathVariable(name = "id") Long productId
    ){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }
}
