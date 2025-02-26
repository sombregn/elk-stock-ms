package com.isi.stock.sales.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDtoRequest {
    private Long id;
    @NotNull(message = "quantity requise")
    private double quantity;
    private LocalDate dateP;
    @NotBlank(message = "La reference de produit est requise")
    private String productId;
    @NotNull(message = "User Id requise")
    private long idUser;

}
