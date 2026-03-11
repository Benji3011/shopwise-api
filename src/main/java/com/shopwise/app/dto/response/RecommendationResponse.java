package com.shopwise.app.dto.response;

import java.math.BigDecimal;

public class RecommendationResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Long score; // total quantity sold OR co-occurrence count

    public RecommendationResponse() {}
    public RecommendationResponse(Long productId, String productName, BigDecimal price, Long score) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.score = score;
    }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getScore() { return score; }
    public void setScore(Long score) { this.score = score; }
}
