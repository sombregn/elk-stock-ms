package com.isi.stock.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoRequest {
    @NotBlank(message = "La reference est requise!")
    private String ref;
    @NotBlank(message = "Le nom est requis!")
    private String name;
    @NotNull(message = "Le stock est requis!")
    private double stock;
    @NotNull(message = "Id User est requis!")
    private long idUser;
}
