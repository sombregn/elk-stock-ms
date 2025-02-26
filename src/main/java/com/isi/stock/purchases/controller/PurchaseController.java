package com.isi.stock.purchases.controller;

import com.isi.stock.purchases.dto.PurchaseDtoRequest;
import com.isi.stock.purchases.dto.PurchaseDtoResponse;
import com.isi.stock.purchases.service.PurchaseService;
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
@RequestMapping("/purchases")
@AllArgsConstructor
@Getter
@Setter
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseDtoResponse> savePurchase(@Valid @RequestBody PurchaseDtoRequest request) {
        logger.info("Tentative de sauvegarde d'un achat : {}", request);
        Optional<PurchaseDtoResponse> purchaseDto = purchaseService.savePurchase(request);
        if (purchaseDto.isPresent()) {
            logger.info("Achat sauvegardé avec succès : {}", purchaseDto.get());
            return new ResponseEntity<>(purchaseDto.get(), HttpStatus.CREATED);
        } else {
            logger.error("Échec de la sauvegarde de l'achat : {}", request);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDtoResponse> getPurchase(@PathVariable Long id) {
        logger.info("Recherche de l'achat avec l'ID : {}", id);
        Optional<PurchaseDtoResponse> purchaseDto = purchaseService.getPurchaseById(id);
        if (purchaseDto.isPresent()) {
            logger.info("Achat trouvé : {}", purchaseDto.get());
            return new ResponseEntity<>(purchaseDto.get(), HttpStatus.OK);
        } else {
            logger.warn("Achat avec ID {} non trouvé", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDtoResponse>> getPurchases() {
        logger.info("Récupération de tous les achats...");
        Optional<List<PurchaseDtoResponse>> purchaseDtos = purchaseService.getAllPurchases();
        if (purchaseDtos.isPresent() && !purchaseDtos.get().isEmpty()) {
            logger.info("Nombre d'achats récupérés : {}", purchaseDtos.get().size());
            return new ResponseEntity<>(purchaseDtos.get(), HttpStatus.OK);
        } else {
            logger.warn("Aucun achat trouvé");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        logger.info("Tentative de suppression de l'achat avec l'ID : {}", id);
        purchaseService.deletePurchase(id);
        logger.info("Achat supprimé avec succès");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDtoResponse> updatePurchase(@PathVariable("id") Long id, @RequestBody @Valid PurchaseDtoRequest purchaseDtoRequest) {
        logger.info("Mise à jour de l'achat avec l'ID : {} | Données : {}", id, purchaseDtoRequest);
        Optional<PurchaseDtoResponse> purchase = purchaseService.updatePurchase(id, purchaseDtoRequest);
        if (purchase.isPresent()) {
            logger.info("Achat mis à jour avec succès : {}", purchase.get());
            return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
        } else {
            logger.warn("Échec de la mise à jour : Achat avec ID {} non trouvé", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
