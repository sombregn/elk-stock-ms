package com.isi.stock.products.entities;

import com.isi.stock.purchases.entities.PurchaseEntity;
import com.isi.stock.sales.entities.SaleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "products")
public class ProductEntity implements Serializable {

    @Id
    @Column(unique = true , nullable = false )
    private String ref;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double stock;

    @Column(nullable = false)
    private long idUser;

    @OneToMany(mappedBy = "product")
    private List<PurchaseEntity> purchases;

    @OneToMany(mappedBy = "product")
    private List<SaleEntity> sales;

}

