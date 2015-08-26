package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.randyp.jdbj.example.Collectors;
import com.google.common.collect.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class Shipping {

    private final long id;
    private final Address address;
    private final ImmutableList<LineItem> lineItems;

    private Shipping(long id, Address address, List<LineItem> lineItems) {
        Objects.requireNonNull(address, "address must not be null");
        Objects.requireNonNull(lineItems, "lineItems must not be null");
        this.id = id;
        this.address = address;
        this.lineItems = ImmutableList.copyOf(lineItems);
    }

    public long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public ImmutableList<LineItem> getLineItems() {
        return lineItems;
    }

    public Builder builder() {
        final ImmutableList<LineItem.Builder> lineItems = this.lineItems.stream()
                .map(LineItem::builder)
                .collect(Collectors.toImmutableList());
        return new Builder(id, address.builder(), lineItems);
    }

    public static class Builder {

        private long id;

        @NotNull
        private Address.Builder address;

        @Valid
        @NotEmpty
        private ImmutableList<LineItem.Builder> lineItems;

        @JsonCreator
        public Builder(@JsonProperty("id") long id,
                       @JsonProperty("address") Address.Builder address,
                       @JsonProperty("lineItems") ImmutableList<LineItem.Builder> lineItems) {
            this.id = id;
            this.address = address;
            this.lineItems = lineItems;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setAddress(Address.Builder address) {
            this.address = address;
            return this;
        }

        public Builder setLineItems(ImmutableList<LineItem.Builder> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public Shipping build() {
            final ImmutableList<LineItem> lineItems = this.lineItems.stream()
                    .map(LineItem.Builder::build)
                    .collect(Collectors.toImmutableList());
            return new Shipping(id, address.build(), lineItems);
        }
    }
}
