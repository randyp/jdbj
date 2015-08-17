package com.github.randyp.jdbj;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Student extends NewStudent {

    public static final String insert_id = "student_insert_id.sql";

    public static final ExecuteQuery<List<Student>> selectAll = JDBJ.string("SELECT * FROM student ORDER BY id ASC").query()
            .map(Student::from)
            .toList();

    public static Student from(SmartResultSet rs) throws SQLException {
        return new Student(rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getBigDecimal("gpa"));
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
}
