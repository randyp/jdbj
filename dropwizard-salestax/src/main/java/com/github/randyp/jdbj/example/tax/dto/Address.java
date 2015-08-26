package com.github.randyp.jdbj.example.tax.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Address {

    private final String streetLine1;
    private final String streetLine2;
    private final String city;
    private final State state;
    private final String postalCode;

    private Address(String streetLine1,
                    String streetLine2,
                    String city,
                    State state,
                    String postalCode) {
        Objects.requireNonNull(city, "city must not be null");
        Objects.requireNonNull(state, "state must not be null");
        this.streetLine1 = streetLine1;
        this.streetLine2 = streetLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public String getStreetLine1() {
        return streetLine1;
    }

    public String getStreetLine2() {
        return streetLine2;
    }

    public String getCity() {
        return city;
    }

    public State getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Builder builder() {
        return new Builder(
                streetLine1,
                streetLine2,
                city,
                state,
                postalCode);
    }

    public static class Builder {

        private String streetLine1;
        private String streetLine2;
        @NotEmpty
        private String city;
        @NotNull
        private State state;
        private String postalCode;

        @JsonCreator
        public Builder(@JsonProperty("streetLine1") String streetLine1,
                       @JsonProperty("streetLine2") String streetLine2,
                       @JsonProperty("city") String city,
                       @JsonProperty("state") State state,
                       @JsonProperty("postalCode") String postalCode) {
            this.streetLine1 = streetLine1;
            this.streetLine2 = streetLine2;
            this.city = city;
            this.state = state;
            this.postalCode = postalCode;
        }

        public Builder setStreetLine1(String streetLine1) {
            this.streetLine1 = streetLine1;
            return this;
        }

        public Builder setStreetLine2(String streetLine2) {
            this.streetLine2 = streetLine2;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setState(State state) {
            this.state = state;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address build() {
            return new Address(streetLine1, streetLine2, city, state, postalCode);
        }
    }
}
