package com.isi.stock.purchases.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDtoRequest {
    private Long id;
    private LocalDate date;
    @NotNull(message = "Quantity requise")
    private double quantity;
    @NotBlank(message = "Reference de produit est requise")
    private String productId;
    @NotNull(message = "Id User est requis!")
    private long idUser;
}
