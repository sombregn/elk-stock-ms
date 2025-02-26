package com.isi.stock.sales.repository;

import com.isi.stock.sales.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity,Long> {
}
