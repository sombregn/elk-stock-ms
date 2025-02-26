package com.isi.stock.products.service;

import com.isi.stock.products.dto.ProductDtoRequest;
import com.isi.stock.products.dto.ProductDtoResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDtoResponse> saveProduct(ProductDtoRequest productDto);
    Optional<List<ProductDtoResponse>> getAllProducts();
    Optional<ProductDtoResponse> getProductByRef(String ref);
    boolean deleteProduct(String ref);
    Optional<ProductDtoResponse> updateProduct(String ref, ProductDtoRequest productDto);

}
