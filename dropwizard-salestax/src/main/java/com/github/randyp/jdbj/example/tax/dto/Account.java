package com.github.randyp.jdbj.example.tax.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Account {

    public static Account from(ResultSet rs) throws SQLException {
        return new Account(rs.getLong("id"), rs.getString("email"));
    }

    private final long id;
    private final String email;

    public Account(long id, String email) {
        Objects.requireNonNull(email, "email must not be null");
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
