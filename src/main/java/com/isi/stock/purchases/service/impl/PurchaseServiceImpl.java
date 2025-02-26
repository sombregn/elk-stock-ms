package com.isi.stock.purchases.service.impl;

import com.isi.stock.exception.EntityNotFoundException;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.purchases.dto.PurchaseDtoRequest;
import com.isi.stock.purchases.dto.PurchaseDtoResponse;
import com.isi.stock.purchases.entities.PurchaseEntity;
import com.isi.stock.purchases.mapper.PurchasesMapper;
import com.isi.stock.purchases.repository.PurchaseRepository;
import com.isi.stock.purchases.service.PurchaseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchasesMapper purchasesMapper;
    private final MessageSource messageSource;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public Optional<PurchaseDtoResponse> savePurchase(PurchaseDtoRequest request) {
        if (productRepository.findByRef(request.getProductId()).isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage("product.notfound", new Object[]{request.getProductId()}, Locale.getDefault()));
        }
        PurchaseEntity purchase = purchasesMapper.toPurchaseEntity(request);
        var purchaseEntity = purchaseRepository.save(purchase);
        return Optional.of(purchasesMapper.toPurchaseDtoResponse(purchaseEntity));
    }

    @Override
    public Optional<List<PurchaseDtoResponse>> getAllPurchases() {
        List<PurchaseEntity> purchases = purchaseRepository.findAll();
        return Optional.of(purchasesMapper.toPurchaseDtoResponseList(purchases));
    }

    @Override
    public Optional<PurchaseDtoResponse> getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .map(purchase -> Optional.of(purchasesMapper.toPurchaseDtoResponse(purchase)))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("purchase.notfound", new Object[]{id}, Locale.getDefault())));
    }

    @Override
    public boolean deletePurchase(Long id) {
        if (purchaseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage("purchase.notfound", new Object[]{id}, Locale.getDefault()));
        }
        purchaseRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<PurchaseDtoResponse> updatePurchase(Long id, PurchaseDtoRequest request) {

        return purchaseRepository.findById(request.getId())
                .map(purchase -> {
                    purchase.setId(request.getId());
                    purchase.setDate(request.getDate());
                    purchase.setQuantity(request.getQuantity());
                    var productEntity = productRepository.findByRef(request.getProductId())
                                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("product.notfound",  new Object[]{request.getProductId()}, Locale.getDefault())));
                    purchase.setProduct(productEntity);
                    purchase.setIdUser(request.getIdUser());
                    var purchaseEntity = purchaseRepository.save(purchase);
                    return Optional.of(purchasesMapper.toPurchaseDtoResponse(purchaseEntity));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("purchase.notfound", new Object[]{request.getId()}, Locale.getDefault())));
    }
}
