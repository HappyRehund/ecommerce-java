package com.rehund.ecommerce.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Nama produk harus antara 2 dan 100 karakter")
    private String name;

    @NotNull(message = "Harga tidak boleh kosong")
    @Positive(message = "Harga harus lebih besar dari 0")
    @Digits(integer = 10, fraction = 2, message = "Harga harus memiliki maksimal 10 digit dan 2 angka dibelakang koma")
    private BigDecimal price;

    @NotNull(message = "Deskripsi produk tidak boleh null")
    @Size(max = 1000, message = "Deskripsi produk tidak boleh lebih dari 1000 karakter")
    private String description;

    @NotNull(message = "stock quantity tidak boleh kosong")
    @Positive(message = "stock quantity harus lebih besar dari 0")
    private Integer stockQuantity;

    @NotNull(message = "weight tidak boleh kosong")
    @Positive(message = "weight harus lebih besar dari 0")
    @Digits(integer = 10, fraction = 2, message = "weight harus memiliki maksimal 10 digit dan 2 angka dibelakang koma")
    private BigDecimal weight;

    private List<Long> categoryIds;
}
