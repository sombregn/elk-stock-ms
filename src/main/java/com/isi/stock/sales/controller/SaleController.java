package com.isi.stock.sales.controller;

import com.isi.stock.sales.dto.SaleDtoRequest;
import com.isi.stock.sales.dto.SaleDtoResponse;
import com.isi.stock.sales.service.SaleService;
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
@RequestMapping("/sales")
@AllArgsConstructor
@Getter
@Setter
public class SaleController {

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleDtoResponse> saveSale(@Valid @RequestBody SaleDtoRequest saleDtoRequest) {
        logger.info("Tentative de sauvegarde d'une vente : {}", saleDtoRequest);
        Optional<SaleDtoResponse> response = saleService.saveSale(saleDtoRequest);

        if (response.isPresent()) {
            logger.info("Vente enregistrée avec succès : {}", response.get());
            return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
        } else {
            logger.error("Échec de l'enregistrement de la vente");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDtoResponse> getSale(@PathVariable Long id) {
        logger.info("Recherche de la vente avec l'ID : {}", id);
        Optional<SaleDtoResponse> saleDtoResponse = saleService.getSaleById(id);

        if (saleDtoResponse.isPresent()) {
            logger.info("Vente trouvée : {}", saleDtoResponse.get());
            return new ResponseEntity<>(saleDtoResponse.get(), HttpStatus.OK);
        } else {
            logger.warn("Vente avec l'ID {} non trouvée", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<SaleDtoResponse>> getAllSales() {
        logger.info("Récupération de toutes les ventes...");
        Optional<List<SaleDtoResponse>> sales = saleService.getAllSales();

        if (sales.isPresent() && !sales.get().isEmpty()) {
            logger.info("Nombre de ventes récupérées : {}", sales.get().size());
            return new ResponseEntity<>(sales.get(), HttpStatus.OK);
        } else {
            logger.warn("Aucune vente trouvée");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable("id") Long id) {
        logger.info("Tentative de suppression de la vente avec l'ID : {}", id);
        saleService.deleteSale(id);
        logger.info("Vente supprimée avec succès");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDtoResponse> updateSale(@PathVariable("id") Long id, @RequestBody @Valid SaleDtoRequest saleDtoRequest) {
        logger.info("Mise à jour de la vente avec l'ID : {} | Données : {}", id, saleDtoRequest);
        Optional<SaleDtoResponse> sale = saleService.updateSale(id, saleDtoRequest);

        if (sale.isPresent()) {
            logger.info("Vente mise à jour avec succès : {}", sale.get());
            return new ResponseEntity<>(sale.get(), HttpStatus.OK);
        } else {
            logger.warn("Échec de la mise à jour : Vente avec ID {} non trouvée", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
