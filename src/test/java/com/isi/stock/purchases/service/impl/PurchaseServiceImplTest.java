package com.isi.stock.purchases.service.impl;

import com.isi.stock.exception.EntityNotFoundException;
import com.isi.stock.products.entities.ProductEntity;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.purchases.dto.PurchaseDtoRequest;
import com.isi.stock.purchases.dto.PurchaseDtoResponse;
import com.isi.stock.purchases.entities.PurchaseEntity;
import com.isi.stock.purchases.mapper.PurchasesMapper;
import com.isi.stock.purchases.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;
    @InjectMocks
    private PurchaseServiceImpl purchaseService;
    @Mock
    private PurchasesMapper purchasesMapper;

    @Mock
    private MessageSource messageSource;
    @Mock
    private ProductRepository productRepository;

    @Test
    void savePurchaseOK() {
        when(productRepository.findByRef(anyString())).thenReturn(Optional.of(this.getProductEntity()));
        when(purchasesMapper.toPurchaseEntity(any())).thenReturn(this.getPurchaseEntity());
        when(purchaseRepository.save(any())).thenReturn(this.getPurchaseEntity());
        when(purchasesMapper.toPurchaseDtoResponse(any())).thenReturn(this.getPurchaseDtoResponse());
        Optional<PurchaseDtoResponse> response = purchaseService.savePurchase(this.getPurchaseDtoRequest());
        assertTrue(response.isPresent());
    }


    @Test
    void savePurchaseKO() {
        when(productRepository.findByRef(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("product.notfound"), any(), any(Locale.class)))
                .thenReturn("The product with ID MAD01 was not found");
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> purchaseService.savePurchase(this.getPurchaseDtoRequest()));
        assertEquals("The product with ID MAD01 was not found", exception.getMessage());
        assertNotNull(exception);
    }


    @Test
    void getPurchaseByIdfOK() {
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(getPurchaseEntity()));
        when(purchasesMapper.toPurchaseDtoResponse(any())).thenReturn(getPurchaseDtoResponse());

        Optional<PurchaseDtoResponse> purchase = purchaseService.getPurchaseById(1L);
        assertTrue(purchase.isPresent());
        assertEquals(1L, purchase.get().getId());
    }

    @Test
    void getPurchaseByIdKO() {
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("purchase.notfound"), any(), any(Locale.class))).thenReturn("Purchase not found");
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseService.getPurchaseById(1L));
        assertEquals("Purchase not found", exception.getMessage());
    }

    @Test
    void getAllPurchases() {
        when(purchaseRepository.findAll()).thenReturn(List.of(this.getPurchaseEntity()));
        when(purchasesMapper.toPurchaseDtoResponseList(any())).thenReturn(List.of(this.getPurchaseDtoResponse()));

        Optional<List<PurchaseDtoResponse>> purchases = purchaseService.getAllPurchases();
        assertTrue(purchases.isPresent());
        assertEquals(1, purchases.get().size());
    }

    @Test
    void deletePurchaseOK() {
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(getPurchaseEntity()));

        boolean result = purchaseService.deletePurchase(anyLong());
        assertTrue(result);
        verify(purchaseRepository, times(1)).deleteById(anyLong());
    }


    @Test
    void deletePurchaseKO() {
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("purchase.notfound"), any(), any(Locale.class)))
                .thenReturn("Purchase not found");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseService.deletePurchase(1L));
        assertEquals("Purchase not found", exception.getMessage());
    }

//    @Test
//    void updatePurchaseOK() {
//        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(getPurchaseEntity()));
//        when(purchaseRepository.save(any())).thenReturn(getPurchaseEntity());
//        when(purchasesMapper.toPurchaseDtoResponse(any())).thenReturn(getPurchaseDtoResponse());
//
//        Optional<PurchaseDtoResponse> updatedPurchase = purchaseService.updatePurchase(1L, getPurchaseDtoRequest());
//        assertTrue(updatedPurchase.isPresent());
//        assertEquals(1L, updatedPurchase.get().getId()); // Vérifie que l'ID de l'achat est correct
//    }

//    @Test
//    void updatePurchaseKO() {
//        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.empty());
//        when(messageSource.getMessage(eq("purchase.notfound"), any(), any(Locale.class)))
//                .thenReturn("Purchase not found");
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> purchaseService.updatePurchase(1L, getPurchaseDtoRequest()));
//        assertEquals("Purchase not found", exception.getMessage());
//    }



    private PurchaseEntity getPurchaseEntity() {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setDate(LocalDate.parse("2022-09-09"));
        purchaseEntity.setQuantity(3);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setRef("MAD01");
        productEntity.setName("Test Product");
        purchaseEntity.setProduct(productEntity);
        purchaseEntity.setIdUser(1);

        return purchaseEntity;
    }


    private PurchaseDtoResponse getPurchaseDtoResponse() {
        PurchaseDtoResponse purchaseDtoResponse = new PurchaseDtoResponse();
        purchaseDtoResponse.setId(1L);
        purchaseDtoResponse.setDate(LocalDate.parse("2022-09-09"));
        purchaseDtoResponse.setQuantity(3);
        purchaseDtoResponse.setIdUser(1);
        purchaseDtoResponse.setProductId("MAD01");
        return purchaseDtoResponse;
    }


    private PurchaseDtoRequest getPurchaseDtoRequest(){
        PurchaseDtoRequest purchaseDtoRequest = new PurchaseDtoRequest();
        purchaseDtoRequest.setDate(LocalDate.parse("2022-09-09"));
        purchaseDtoRequest.setQuantity(3);
        purchaseDtoRequest.setProductId("MAD01");
        purchaseDtoRequest.setIdUser(1);
        return purchaseDtoRequest;
    }

    private ProductEntity getProductEntity(){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setRef("MAD01");
        productEntity.setName("Madar");
        productEntity.setStock(100.0);
        productEntity.setIdUser(1);

        return productEntity;
    }
}