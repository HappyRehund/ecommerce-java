package com.rehund.ecommerce.service;

import com.rehund.ecommerce.model.PaginatedProductResponse;
import com.rehund.ecommerce.model.ProductRequest;
import com.rehund.ecommerce.model.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductResponse> findAll();

    Page<ProductResponse> findByPage(Pageable pageRequest);

    Page<ProductResponse> findByNameAndPageable(String name, Pageable pageable);

    ProductResponse findById(Long productId);

    ProductResponse create(ProductRequest productRequest);

    ProductResponse update(Long productId, ProductRequest productRequest);

    void delete(Long productId);

    PaginatedProductResponse convertProductPage(Page<ProductResponse> responses);

}
