package com.github.randyp.jdbj.example.tax.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDTO {

    public static AccountDTO from(ResultSet rs) throws SQLException {
        return new AccountDTO(rs.getLong("id"), rs.getString("email"));
    }

    private final long id;
    private final String email;

    public AccountDTO(long id, String email) {
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
