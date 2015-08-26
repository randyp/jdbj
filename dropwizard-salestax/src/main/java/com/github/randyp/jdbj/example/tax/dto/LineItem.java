package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.randyp.jdbj.example.tax.Cost;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class LineItem {
    
    private final long id;
    private final long sku;
    private final Cost cost;

    private LineItem(long id, long sku, Cost cost) {
        Objects.requireNonNull(cost, "cost must not be null");
        this.id = id;
        this.sku = sku;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public long getSku() {
        return sku;
    }

    public Cost getCost() {
        return cost;
    }
    
    public Builder builder(){
        return new Builder(id, sku, cost);
    }
    
    public static class Builder {

        private long id;
        
        private long sku;
        
        @NotNull
        private Cost cost;

        @JsonCreator
        public Builder(@JsonProperty("id") long id,
                       @JsonProperty("sku") long sku,
                       @JsonProperty("cost") Cost cost) {
            this.id = id;
            this.sku = sku;
            this.cost = cost;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setSku(long sku) {
            this.sku = sku;
            return this;
        }

        public Builder setCost(Cost cost) {
            this.cost = cost;
            return this;
        }
        
        public LineItem build(){
            return new LineItem(id, sku, cost);
        }
    }
}
