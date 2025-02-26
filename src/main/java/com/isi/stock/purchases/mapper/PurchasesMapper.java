package com.isi.stock.purchases.mapper;


import com.isi.stock.products.entities.ProductEntity;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.purchases.dto.PurchaseDtoRequest;
import com.isi.stock.purchases.dto.PurchaseDtoResponse;
import com.isi.stock.purchases.entities.PurchaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductRepository.class})
public interface PurchasesMapper {

    @Mapping(source = "productId", target = "product", qualifiedByName = "mapProductRefToProductEntity")
    PurchaseEntity toPurchaseEntity(PurchaseDtoRequest purchaseDto);

    @Mapping(source = "product.ref", target = "productId")
    PurchaseDtoResponse toPurchaseDtoResponse(PurchaseEntity purchaseEntity);

    List<PurchaseDtoResponse> toPurchaseDtoResponseList(List<PurchaseEntity> purchaseEntityList);

    List<PurchaseEntity> toProductEntityList(List<PurchaseDtoResponse> purchaseDtoResponseList);

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

