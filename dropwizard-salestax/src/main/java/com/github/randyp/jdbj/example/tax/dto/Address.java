package com.github.randyp.jdbj.example.tax.dto;

import java.util.Objects;

public class Address {
    
    private final String streetLine1;
    private final String streetLine2;
    private final String city;
    private final State state;
    private final String postalCode;

    public Address(String streetLine1, String streetLine2, String city, State state, String postalCode) {
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
}
