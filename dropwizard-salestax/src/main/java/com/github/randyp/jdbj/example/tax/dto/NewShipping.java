package com.github.randyp.jdbj.example.tax.dto;

import java.util.Objects;

public class NewShipping {

    private final Address address;

    public NewShipping(Address address) {
        Objects.requireNonNull(address, "address must not be null");
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}
