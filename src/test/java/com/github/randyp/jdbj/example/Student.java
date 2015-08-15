package com.github.randyp.jdbj.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {

    public static Student from(ResultSet rs) throws SQLException {
        return new Student(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDouble("gpa"));
    }

    public final long id;
    public final String firstName;
    public final String lastName;
    public final double gpa;

    public Student(long id, String firstName, String lastName, double gpa) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
    }
}
