package com.rehund.ecommerce.repository;

import com.rehund.ecommerce.entity.ProductCategory;
import com.rehund.ecommerce.entity.ProductCategory.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {

}
