package com.isi.stock.products.service.impl;

import com.isi.stock.exception.EntityExistsException;
import com.isi.stock.exception.EntityNotFoundException;
import com.isi.stock.products.dto.ProductDtoRequest;
import com.isi.stock.products.dto.ProductDtoResponse;
import com.isi.stock.products.entities.ProductEntity;
import com.isi.stock.products.mapper.ProductsMapper;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductsMapper productsMapper;
    private final MessageSource messageSource;


    @Override
    @Transactional
    public Optional<ProductDtoResponse> saveProduct(ProductDtoRequest productDto) {

        if (productRepository.findByRef(productDto.getRef()).isPresent()) {
            throw new EntityExistsException(messageSource.getMessage("product.exists", new Object[]{productDto.getRef()}, Locale.getDefault()));
        }
        ProductEntity product = productsMapper.toProductEntity(productDto);
        var productEntity = productRepository.save(product);
        return Optional.of(productsMapper.toProductDtoResponse(productEntity));

//        var productEntity = productRepository.save(productMapper.toProductEntity(productDto));
//        return (productEntity != null) ? Optional.of(productMapper.toProductDto(productEntity)) : Optional.empty();
    }



    @Override
    public Optional<ProductDtoResponse> getProductByRef(String ref) {
        log.info("getProductByRef: " + ref);
        return productRepository.findByRef(ref)
                .map(product -> Optional.of(productsMapper.toProductDtoResponse(product)))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("product.notfound", new Object[]{ref}, Locale.getDefault())));
    }

    @Override
    public Optional<List<ProductDtoResponse>> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return Optional.of(productsMapper.toProductDtoResponseList(products));
    }

    @Override
    public boolean deleteProduct(String ref) {
        if (productRepository.findByRef(ref).isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage("product.notfound", new Object[]{ref}, Locale.getDefault()));
        }
        productRepository.deleteById(ref);
        return true;
    }

    @Override
    public Optional<ProductDtoResponse> updateProduct(String ref, ProductDtoRequest productDto) {
        return productRepository.findByRef(ref) 
                .map(product -> {
                   
                    product.setName(productDto.getName());
                    product.setStock(productDto.getStock());
                    product.setIdUser(productDto.getIdUser());

                    var productEntity = productRepository.save(product);
                    return Optional.of(productsMapper.toProductDtoResponse(productEntity));
                }).orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("product.notfound", new Object[]{ref}, Locale.getDefault())
                ));
    }



}

