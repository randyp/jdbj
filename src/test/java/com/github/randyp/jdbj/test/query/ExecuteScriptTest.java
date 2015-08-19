package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.Student;
import com.github.randyp.jdbj.StudentTest;
import com.github.randyp.jdbj.test.binding.value.DBSupplier;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteScriptTest implements DBSupplier {

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void insertTwoNoParams() throws Exception {
        final List<Student> expected = Arrays.asList(
                new Student(10, "Ada10", "Dada10", new BigDecimal("3.1")),
                new Student(11L, "Ada11", "Dada11", new BigDecimal("3.2"))
        );

        JDBJ.resource("student_insert_ada10_ada11.sql").script().execute(db());
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(expected, actual);
    }

    @Test
    public void insertTwoParameters() throws Exception {
        final List<Student> expected = Arrays.asList(
                new Student(10, "Ada10", "Dada10", new BigDecimal("3.1")),
                new Student(11L, "Ada11", "Dada11", new BigDecimal("3.2"))
        );

        JDBJ.script(
                "INSERT INTO student(id, first_name, last_name, gpa) VALUES(:id0, 'Ada10', 'Dada10', '3.1');\n" +
                        "INSERT INTO student(id, first_name, last_name, gpa) VALUES(:id1, 'Ada11', 'Dada11', '3.2');"
        ).bindLong(":id0", expected.get(0).getId())
                .bindLong(":id1", expected.get(1).getId())
                .execute(db());
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(expected, actual);
    }

    @Test
    public void convenienceMethodOnJDBJ() throws Exception {
        JDBJ.script("INSERT INTO student(id, first_name, last_name, gpa) VALUES (10 ,'a', 'b', '3.1');")
                .execute(db());
        assertEquals(1, Student.selectAll.execute(db()).size());
    }
}
