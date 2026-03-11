package com.shopwise.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopwise.app.dto.request.CreateSaleRequest;
import com.shopwise.app.dto.request.SaleItemRequest;
import com.shopwise.app.dto.response.SaleItemResponse;
import com.shopwise.app.dto.response.SaleResponse;
import com.shopwise.app.entity.Product;
import com.shopwise.app.entity.Sale;
import com.shopwise.app.entity.SaleItem;
import com.shopwise.app.exception.NotFoundException;
import com.shopwise.app.repository.ProductRepository;
import com.shopwise.app.repository.SaleRepository;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public SaleResponse create(CreateSaleRequest request) {
        Sale sale = new Sale();

        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new NotFoundException("Produit introuvable : " + itemReq.getProductId()));

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            // on prend le prix actuel du catalogue au moment de la vente
            item.setUnitPrice(product.getPrice());
            sale.getItems().add(item);
        }

        return toResponse(saleRepository.save(sale));
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getById(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Vente introuvable : " + id));
        return toResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAll() {
        return saleRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    private SaleResponse toResponse(Sale sale) {
        SaleResponse resp = new SaleResponse();
        resp.setId(sale.getId());
        resp.setCreatedAt(sale.getCreatedAt());

        List<SaleItemResponse> items = sale.getItems().stream().map(item -> {
            SaleItemResponse ir = new SaleItemResponse();
            ir.setId(item.getId());
            ir.setProductId(item.getProduct().getId());
            ir.setProductName(item.getProduct().getName());
            ir.setQuantity(item.getQuantity());
            ir.setUnitPrice(item.getUnitPrice());
            return ir;
        }).toList();

        resp.setItems(items);
        resp.setTotal(
            items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return resp;
    }
}
