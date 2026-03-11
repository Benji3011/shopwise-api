package com.shopwise.app.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class CreateSaleRequest {

    @NotEmpty(message = "A sale must contain at least one item")
    @Valid
    private List<SaleItemRequest> items;

    public CreateSaleRequest() {}
    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }
}
