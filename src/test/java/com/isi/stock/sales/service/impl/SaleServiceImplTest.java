package com.isi.stock.sales.service.impl;

import com.isi.stock.exception.EntityNotFoundException;
import com.isi.stock.products.entities.ProductEntity;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.sales.dto.SaleDtoRequest;
import com.isi.stock.sales.dto.SaleDtoResponse;
import com.isi.stock.sales.entities.SaleEntity;
import com.isi.stock.sales.mapper.SalesMapper;
import com.isi.stock.sales.repository.SaleRepository;
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
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;
    @InjectMocks
    private SaleServiceImpl saleService;
    @Mock
    private SalesMapper salesMapper;
    @Mock
    private MessageSource messageSource;

    @Mock
    private ProductRepository productRepository;

    @Test
    void saveSaleOK() {
        when(productRepository.findByRef(anyString())).thenReturn(Optional.of(this.getProductEntity()));
        when(salesMapper.toSaleEntity(any())).thenReturn(this.getSaleEntity());
        when(saleRepository.save(any())).thenReturn(this.getSaleEntity());
        when(salesMapper.toSaleDtoResponse(any())).thenReturn(this.getSaleDtoResponse());
        Optional<SaleDtoResponse> response = saleService.saveSale(this.getSaleDtoRequest());
        assertTrue(response.isPresent());
    }

    @Test
    void saveSaleKO() {
        when(productRepository.findByRef(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("product.notfound"), any(), any(Locale.class)))
                .thenReturn("The product with ID MAD01 was not found");
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> saleService.saveSale(this.getSaleDtoRequest()));
        assertEquals("The product with ID MAD01 was not found", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void getSaleByIdfOK() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(getSaleEntity()));
        when(salesMapper.toSaleDtoResponse(any())).thenReturn(getSaleDtoResponse());

        Optional<SaleDtoResponse> sale = saleService.getSaleById(1L);
        assertTrue(sale.isPresent());
        assertEquals(1L, sale.get().getId());
    }

    @Test
    void getSaleByIdKO() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("sale.notfound"), any(), any(Locale.class))).thenReturn("Sale not found");
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> saleService.getSaleById(1L));
        assertEquals("Sale not found", exception.getMessage());
    }

    @Test
    void getAllSalles() {
        when(saleRepository.findAll()).thenReturn(List.of(this.getSaleEntity()));
        when(salesMapper.toSaleDtoResponseList(any())).thenReturn(List.of(this.getSaleDtoResponse()));

        Optional<List<SaleDtoResponse>> sales = saleService.getAllSales();
        assertTrue(sales.isPresent());
        assertEquals(1, sales.get().size());
    }

//    @Test
//    void updateSaleOK() {
//        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(getSaleEntity()));
//        when(saleRepository.save(any())).thenReturn(getSaleEntity());
//        when(salesMapper.toSaleDtoResponse(any())).thenReturn(getSaleDtoResponse());
//
//        Optional<SaleDtoResponse> updateSale = saleService.updateSale(1L, getSaleDtoRequest());
//        assertTrue(updateSale.isPresent());
//        assertEquals(1L, updateSale.get().getId()); // Vérifie que l'ID de l'achat est correct
//    }

//    @Test
//    void updateSaleKO() {
//        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
//        when(messageSource.getMessage(eq("sale.notfound"), any(), any(Locale.class)))
//                .thenReturn("Sale not found");
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> saleService.updateSale(1L, getSaleDtoRequest()));
//        assertEquals("Sale not found", exception.getMessage());
//    }


    @Test
    void deleteSaleOK() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(getSaleEntity()));

        boolean result = saleService.deleteSale(anyLong());
        assertTrue(result);
        verify(saleRepository, times(1)).deleteById(anyLong());
    }


    @Test
    void deletePurchaseKO() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("sale.notfound"), any(), any(Locale.class)))
                .thenReturn("Sale not found");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> saleService.deleteSale(1L));
        assertEquals("Sale not found", exception.getMessage());
    }


    private SaleEntity getSaleEntity() {
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setDateP(LocalDate.parse("2022-09-09"));
        saleEntity.setQuantity(3);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setRef("MAD01");
        productEntity.setName("Test Product");
        saleEntity.setProduct(productEntity);
        saleEntity.setIdUser(1);

        return saleEntity;
    }


    private SaleDtoResponse getSaleDtoResponse() {
        SaleDtoResponse saleDtoResponse = new SaleDtoResponse();
        saleDtoResponse.setId(1L);
        saleDtoResponse.setDateP(LocalDate.parse("2022-09-09"));
        saleDtoResponse.setQuantity(3);
        saleDtoResponse.setIdUser(1);
        saleDtoResponse.setProductId("MAD01");
        return saleDtoResponse;
    }


    private SaleDtoRequest getSaleDtoRequest(){
        SaleDtoRequest saleDtoRequest = new SaleDtoRequest();
        saleDtoRequest.setDateP(LocalDate.parse("2022-09-09"));
        saleDtoRequest.setQuantity(3);
        saleDtoRequest.setProductId("MAD01");
        saleDtoRequest.setIdUser(1);
        return saleDtoRequest;
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