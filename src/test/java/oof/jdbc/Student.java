package oof.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {

    public static Student from(ResultSet rs) throws SQLException {
        return new Student(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDouble("gpa"));
    }

    private final long id;
    private final String firstName;
    private final String lastName;
    private final double gpa;



    public Student(long id, String firstName, String lastName, double gpa) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getGpa() {
        return gpa;
    }
}
