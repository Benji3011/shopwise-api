package com.shopwise.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopwise.app.dto.response.RecommendationResponse;
import com.shopwise.app.exception.NotFoundException;
import com.shopwise.app.repository.ProductRepository;
import com.shopwise.app.repository.SaleItemRepository;

@Service
@Transactional(readOnly = true)
public class RecommendationServiceImpl implements RecommendationService {

    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;

    public RecommendationServiceImpl(SaleItemRepository saleItemRepository, ProductRepository productRepository) {
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;
    }

    // top produits = ceux qui ont le plus grand volume vendu (sum des quantités)
    @Override
    public List<RecommendationResponse> getTopProducts(int limit) {
        int n = Math.max(1, Math.min(limit, 50));
        return saleItemRepository.findTopProducts(n).stream()
            .map(row -> new RecommendationResponse(
                (Long) row[0],
                (String) row[1],
                (BigDecimal) row[2],
                (Long) row[3]
            ))
            .toList();
    }

    // produits similaires = co-occurrence dans les mêmes ventes
    // idée : si A et B sont souvent achetés ensemble, B est pertinent quand on regarde A
    @Override
    public List<RecommendationResponse> getSimilarProducts(Long productId, int limit) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Produit introuvable : " + productId);
        }
        int n = Math.max(1, Math.min(limit, 50));
        return saleItemRepository.findSimilarProducts(productId, n).stream()
            .map(row -> new RecommendationResponse(
                (Long) row[0],
                (String) row[1],
                (BigDecimal) row[2],
                (Long) row[3]
            ))
            .toList();
    }
}
