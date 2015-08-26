package com.github.randyp.jdbj.example.tax.dto;

import java.util.Objects;

public enum State {
    
    CA("CA", "California");

    private final String abbreviation;
    private final String name;

    State(String abbreviation, String name) {
        Objects.requireNonNull(abbreviation, "abbreviation must not be null");
        Objects.requireNonNull(name, "name must not be null");
        this.abbreviation = abbreviation;
        this.name = name;
    }
}
