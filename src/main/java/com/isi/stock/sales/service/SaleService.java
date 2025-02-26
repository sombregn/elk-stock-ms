package com.isi.stock.sales.service;

import com.isi.stock.sales.dto.SaleDtoRequest;
import com.isi.stock.sales.dto.SaleDtoResponse;

import java.util.List;
import java.util.Optional;

public interface SaleService {

    Optional<SaleDtoResponse> saveSale(SaleDtoRequest request);
    Optional<List<SaleDtoResponse>> getAllSales();
    Optional<SaleDtoResponse> getSaleById(Long id);
    boolean deleteSale(Long id);
    Optional<SaleDtoResponse> updateSale(Long id, SaleDtoRequest request);

}
