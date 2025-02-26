package com.isi.stock.sales.service.impl;

import com.isi.stock.exception.EntityNotFoundException;
import com.isi.stock.products.repository.ProductRepository;
import com.isi.stock.sales.dto.SaleDtoRequest;
import com.isi.stock.sales.dto.SaleDtoResponse;
import com.isi.stock.sales.entities.SaleEntity;
import com.isi.stock.sales.mapper.SalesMapper;
import com.isi.stock.sales.repository.SaleRepository;
import com.isi.stock.sales.service.SaleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter
@Setter
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SalesMapper salesMapper;
    private final MessageSource messageSource;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public Optional<SaleDtoResponse> saveSale(SaleDtoRequest request) {
        if (productRepository.findByRef(request.getProductId()).isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage("product.notfound", new Object[]{request.getProductId()}, Locale.getDefault()));
        }

        SaleEntity sale = salesMapper.toSaleEntity(request);
        var saleEntity = saleRepository.save(sale);
        return Optional.of(salesMapper.toSaleDtoResponse(saleEntity));
    }

    @Override
    public Optional<List<SaleDtoResponse>> getAllSales() {
        List<SaleEntity> sales = saleRepository.findAll();
        return Optional.of(salesMapper.toSaleDtoResponseList(sales));
    }

    @Override
    public Optional<SaleDtoResponse> getSaleById(Long id) {
       return saleRepository.findById(id)
               .map(sale -> Optional.of(salesMapper.toSaleDtoResponse(sale)))
               .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("sale.notfound", new Object[]{id}, Locale.getDefault())));
    }

    @Override
    public boolean deleteSale(Long id) {
        if (saleRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(messageSource.getMessage("sale.notfound", new Object[]{id}, Locale.getDefault()));
        }
        saleRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<SaleDtoResponse> updateSale(Long id, SaleDtoRequest request) {
        return saleRepository.findById(request.getId())
                .map(sale -> {
                    sale.setId(request.getId());
                    sale.setQuantity(request.getQuantity());
                    sale.setDateP(request.getDateP());
                    var productEntity = productRepository.findByRef(request.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("product.notfound",  new Object[]{request.getProductId()}, Locale.getDefault())));
                    sale.setProduct(productEntity);
                    sale.setIdUser(request.getIdUser());
                    var saleEntity = saleRepository.save(sale);
                    return Optional.of(salesMapper.toSaleDtoResponse(saleEntity));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("sale.notfound", new Object[]{request.getId()}, Locale.getDefault())));
    }
}
