package com.shopwise.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopwise.app.dto.response.RecommendationResponse;
import com.shopwise.app.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * GET /api/recommendations/top?limit=5
     * Returns the top N best-selling products.
     */
    @GetMapping("/top")
    public ResponseEntity<List<RecommendationResponse>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.getTopProducts(limit));
    }

    /**
     * GET /api/recommendations/similar/{productId}?limit=5
     * Returns products frequently bought together with the given product.
     */
    @GetMapping("/similar/{productId}")
    public ResponseEntity<List<RecommendationResponse>> getSimilarProducts(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.getSimilarProducts(productId, limit));
    }
}
