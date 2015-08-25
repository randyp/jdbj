package com.github.randyp.jdbj.example.tax.dto;

import com.github.randyp.jdbj.example.tax.Cost;

public class NewLineItem {
    
    private final long sku;
    private final Cost cost;

    public NewLineItem(long sku, Cost cost) {
        if (cost == null) {
            throw new IllegalArgumentException("cost should not be null");
        }
        this.sku = sku;
        this.cost = cost;
    }

    public long getSku() {
        return sku;
    }

    public Cost getCost() {
        return cost;
    }
}
