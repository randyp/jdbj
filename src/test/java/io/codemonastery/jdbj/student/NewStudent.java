package io.codemonastery.jdbj.student;

import io.codemonastery.jdbj.ValueBindings;

import java.math.BigDecimal;

public class NewStudent {

    public static final String INSERT = "student_insert.sql";

    final String firstName;
    final String lastName;
    final BigDecimal gpa;

    public NewStudent(String firstName, String lastName, BigDecimal gpa) {
        if (firstName == null) {
            throw new IllegalArgumentException();
        }
        if (lastName == null) {
            throw new IllegalArgumentException();
        }
        if (gpa == null) {
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa.setScale(2, BigDecimal.ROUND_DOWN);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getGpa() {
        return gpa;
    }
    
    public Student withId(long id) {
        return new Student(id, firstName, lastName, gpa);
    }

    public ValueBindings bindings() {
        return new ValueBindings()
                .bindString(":first_name", firstName)
                .bindString(":last_name", lastName)
                .bindBigDecimal(":gpa", gpa);
    }
}
