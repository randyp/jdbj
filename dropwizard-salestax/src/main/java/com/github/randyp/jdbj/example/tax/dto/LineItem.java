package com.github.randyp.jdbj.example.tax.dto;

import com.github.randyp.jdbj.example.tax.Cost;

public class LineItem extends NewLineItem {
    
    private final long id;
    private final long shippingId;

    public LineItem(long id, long shippingId, long sku, Cost cost) {
        super(sku, cost);
        this.id = id;
        this.shippingId = shippingId;
    }

    public long getId() {
        return id;
    }

    public long getShippingId() {
        return shippingId;
    }
}
