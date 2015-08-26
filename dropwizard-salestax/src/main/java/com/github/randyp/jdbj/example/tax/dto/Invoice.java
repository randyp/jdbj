package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.randyp.jdbj.example.Collectors;
import com.google.common.collect.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Invoice {

    private final long id;
    private final ImmutableList<Shipping> shippings;
    private final Date date;

    private Invoice(long id, List<Shipping> shippings, Date date) {
        Objects.requireNonNull(shippings, "shippings must not be null");
        Objects.requireNonNull(date, "date must not be null");
        this.id = id;
        this.shippings = ImmutableList.copyOf(shippings);
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public ImmutableList<Shipping> getShippings() {
        return shippings;
    }

    public Date getDate() {
        return date;
    }

    public Builder builder() {
        final ImmutableList<Shipping.Builder> shippings = this.shippings.stream()
                .map(Shipping::builder)
                .collect(Collectors.toImmutableList());
        return new Builder(id, shippings, date);
    }

    public static class Builder {

        private long id;

        @Valid
        @NotEmpty
        private ImmutableList<Shipping.Builder> shippings;

        @NotNull
        private Date date;

        @JsonCreator
        private Builder(@JsonProperty("id") long id,
                        @JsonProperty("shippings") ImmutableList<Shipping.Builder> shippings,
                        @JsonProperty("date") Date date) {
            this.id = id;
            this.shippings = shippings;
            this.date = date;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder shippings(ImmutableList<Shipping.Builder> shippings) {
            this.shippings = shippings;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Invoice build() {
            final ImmutableList<Shipping> shippings = this.shippings.stream()
                    .map(Shipping.Builder::build)
                    .collect(Collectors.toImmutableList());
            return new Invoice(id, shippings, date);
        }
    }
}
