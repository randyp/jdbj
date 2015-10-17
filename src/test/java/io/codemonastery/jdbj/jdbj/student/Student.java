package io.codemonastery.jdbj.jdbj.student;

import io.codemonastery.jdbj.jdbj.ExecuteQuery;
import io.codemonastery.jdbj.jdbj.JDBJ;
import io.codemonastery.jdbj.jdbj.SmartResult;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class Student extends NewStudent {

    public static final ExecuteQuery<List<Student>> SELECT_ALL = JDBJ.query("SELECT * FROM student ORDER BY id ASC")
            .map(Student::from)
            .toList();

    public static Student from(SmartResult result) throws SQLException {
        return new Student(result.getLongPrimitive("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getBigDecimal("gpa"));
    }

    final long id;

    public Student(long id, String firstName, String lastName, BigDecimal gpa) {
        super(firstName, lastName, gpa);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return id == student.id &&
                firstName.equals(student.firstName) &&
                lastName.equals(student.lastName) &&
                gpa.equals(student.gpa);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + gpa.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ",first_name=" + getFirstName() +
                ",last_name=" + getLastName() +
                ",gpa=" + getGpa() +
                '}';
    }

    public static class Compare {

        public static Comparator<Student> FIRST_NAME = (o1, o2) -> o1.firstName.compareTo(o2.firstName);

        private Compare() {
        }
    }
}
