package com.isi.stock.sales.mapper;


import com.isi.stock.products.entities.ProductEntity;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.sales.dto.SaleDtoRequest;
import com.isi.stock.sales.dto.SaleDtoResponse;
import com.isi.stock.sales.entities.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductRepository.class} )
public interface SalesMapper {

    @Mapping(source = "productId", target = "product", qualifiedByName = "mapProductRefToProductEntity")
    SaleEntity toSaleEntity(SaleDtoRequest saleDtoRequest);
    @Mapping(source = "product.ref", target = "productId")
    SaleDtoResponse toSaleDtoResponse(SaleEntity saleEntity);
    List<SaleDtoResponse> toSaleDtoResponseList(List<SaleEntity> saleEntityList);
    List<SaleEntity> toSaleEntityList(List<SaleDtoResponse> saleDtoResponseList);

    @Named("mapProductRefToProductEntity")
    static ProductEntity mapProductRefToProductEntity(String productRef) {
        if (productRef == null || productRef.isEmpty()) {
            return null;
        }
        ProductEntity product = new ProductEntity();
        product.setRef(productRef);
        return product;
    }
}
