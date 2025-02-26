package com.isi.stock.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDtoResponse {
    private Long id;
    private double quantity;
    private LocalDate dateP;
    private String productId;
    private long idUser;
}
