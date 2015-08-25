package com.github.randyp.jdbj.example.tax.dto;

import com.google.common.collect.ImmutableList;

import java.util.Objects;

public class Shipping extends NewShipping {
    
    private final long id;
    private final ImmutableList<LineItem> lineItems;

    public Shipping(long id, Address address, ImmutableList<LineItem> lineItems) {
        super(address);
        Objects.requireNonNull(lineItems, "lineItems must not be null");
        this.id = id;
        this.lineItems = lineItems;
    }

    public long getId() {
        return id;
    }

    public ImmutableList<LineItem> getLineItems() {
        return lineItems;
    }
}
