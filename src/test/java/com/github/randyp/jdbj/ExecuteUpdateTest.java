package com.github.randyp.jdbj;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExecuteUpdateTest extends StudentTest {

    @Test(expected = IllegalStateException.class)
    public void insertQueryNotEnoughBindings() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert_id).update();
        try (Connection connection = db.getConnection()) {
            executeUpdate.execute(connection);
        }
    }

    @Test
    public void insertOne() throws Exception {
        final Student expected = new Student(10L, "Ada", "Dada", new BigDecimal("3.1"));
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert_id).update()
                .bindLong(":id", expected.id)
                .bindString(":first_name", expected.firstName)
                .bindString(":last_name", expected.lastName)
                .bindBigDecimal(":gpa", expected.gpa);

        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            assertEquals(1, executeUpdate.execute(connection));
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(Collections.singletonList(expected), actual);
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.update("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        try (Connection connection = db.getConnection()) {
            assertEquals(1, executeUpdate.execute(connection));
        }
    }

}
