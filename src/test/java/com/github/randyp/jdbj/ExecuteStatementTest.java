package com.github.randyp.jdbj;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExecuteStatementTest extends StudentTest {

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void insertOne() throws Exception {
        final Student expected = new Student(10, "Ada10", "Dada10", new BigDecimal("3.1"));
        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            JDBJ.resource("student_insert_ada10.sql").statement()
                    .execute(connection);

            actual = Student.selectAll.execute(connection);
        }
        assertEquals(Collections.singletonList(expected), actual);
    }

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void convenienceMethodOnJDBJ() throws Exception {
        final ExecuteStatement executeUpdate = JDBJ.statement("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        try (Connection connection = db.getConnection()) {
            executeUpdate.execute(connection);
            assertEquals(1, Student.selectAll.execute(connection).size());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        try (Connection connection = db.getConnection()) {
            JDBJ.resource(NewStudent.insert).statement()
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .execute(connection);
        }
    }


}
