package com.isi.stock.purchases.service;

import com.isi.stock.purchases.dto.PurchaseDtoRequest;
import com.isi.stock.purchases.dto.PurchaseDtoResponse;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Optional<PurchaseDtoResponse> savePurchase(PurchaseDtoRequest request);
    Optional<List<PurchaseDtoResponse>> getAllPurchases();
    Optional<PurchaseDtoResponse> getPurchaseById(Long id);
    boolean deletePurchase(Long id);
    Optional<PurchaseDtoResponse> updatePurchase(Long id, PurchaseDtoRequest request);
}
