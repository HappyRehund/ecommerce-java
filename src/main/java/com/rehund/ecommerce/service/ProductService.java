package com.rehund.ecommerce.service;

import com.rehund.ecommerce.model.ProductRequest;
import com.rehund.ecommerce.model.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> findAll();

    ProductResponse findById(Long productId);

    ProductResponse create(ProductRequest productRequest);

    ProductResponse update(Long productId, ProductRequest productRequest);

    void delete(Long productId);

}
