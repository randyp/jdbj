package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.ExecuteUpdate;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.Student;
import com.github.randyp.jdbj.StudentTest;
import com.github.randyp.jdbj.test.binding.value.DBSupplier;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteUpdateTest implements DBSupplier {

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void insertQueryNotEnoughBindings() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert_id).update();
        try (Connection connection = db().getConnection()) {
            executeUpdate.execute(connection);
        }
    }

    @Test
    public void insertOne() throws Exception {
        final Student expected = new Student(10L, "Ada", "Dada", new BigDecimal("3.1"));
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert_id).update()
                .bindLong(":id", expected.getId())
                .bindString(":first_name", expected.getFirstName())
                .bindString(":last_name", expected.getLastName())
                .bindBigDecimal(":gpa", expected.getGpa());

        assertEquals(1, executeUpdate.execute(db()));
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(Collections.singletonList(expected), actual);
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.update("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        assertEquals(1, executeUpdate.execute(db()));
    }

}
