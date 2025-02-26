package com.isi.stock.products.controller;

import com.isi.stock.products.dto.ProductDtoRequest;
import com.isi.stock.products.dto.ProductDtoResponse;
import com.isi.stock.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/products")
@Getter
@Setter
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDtoResponse> saveProduct(@RequestBody @Valid ProductDtoRequest productDto) {
        logger.info("Tentative de sauvegarde du produit : {}", productDto);
        Optional<ProductDtoResponse> productDto1 = productService.saveProduct(productDto);
        if (productDto1.isPresent()) {
            logger.info("Produit sauvegardé avec succès : {}", productDto1.get());
            return new ResponseEntity<>(productDto1.get(), HttpStatus.CREATED);
        } else {
            logger.error("Échec de la sauvegarde du produit : {}", productDto);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{ref}")
    public ResponseEntity<ProductDtoResponse> getProduct(@PathVariable("ref") String ref) {
        logger.info("Récupération du produit avec la référence : {}", ref);
        Optional<ProductDtoResponse> productDto1 = productService.getProductByRef(ref);
        if (productDto1.isPresent()) {
            logger.info("Produit trouvé : {}", productDto1.get());
            return new ResponseEntity<>(productDto1.get(), HttpStatus.OK);
        } else {
            logger.warn("Aucun produit trouvé avec la référence : {}", ref);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDtoResponse>> allProducts() {
        logger.info("Récupération de tous les produits");
        Optional<List<ProductDtoResponse>> productDtos = productService.getAllProducts();
        if (productDtos.isPresent() && !productDtos.get().isEmpty()) {
            logger.info("Nombre de produits trouvés : {}", productDtos.get().size());
            return new ResponseEntity<>(productDtos.get(), HttpStatus.OK);
        } else {
            logger.warn("Aucun produit trouvé dans la base de données");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{ref}")
    public ResponseEntity<ProductDtoResponse> updateProduct(@PathVariable("ref") String ref, @RequestBody @Valid ProductDtoRequest productDto) {
        logger.info("Mise à jour du produit avec la référence : {}", ref);
        Optional<ProductDtoResponse> productDto1 = productService.updateProduct(ref, productDto);
        if (productDto1.isPresent()) {
            logger.info("Produit mis à jour avec succès : {}", productDto1.get());
            return new ResponseEntity<>(productDto1.get(), HttpStatus.OK);
        } else {
            logger.error("Échec de la mise à jour du produit avec la référence : {}", ref);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{ref}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("ref") String ref) {
        logger.info("Suppression du produit avec la référence : {}", ref);
        boolean deleted = productService.deleteProduct(ref);
        if (deleted) {
            logger.info("Produit supprimé avec succès : {}", ref);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("Échec de la suppression du produit avec la référence : {}", ref);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
