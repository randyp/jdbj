package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.test.binding.value.DBSupplier;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteStatementTest implements DBSupplier {

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void insertOne() throws Exception {
        final Student expected = new Student(10, "Ada10", "Dada10", new BigDecimal("3.1"));
        JDBJ.resource("student_insert_ada10.sql").statement().execute(db());
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(Collections.singletonList(expected), actual);
    }

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void convenienceMethodOnJDBJ() throws Exception {
        final ExecuteStatement executeUpdate = JDBJ.statement("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        executeUpdate.execute(db());
        assertEquals(1, Student.selectAll.execute(db()).size());
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));
        JDBJ.resource(NewStudent.insert).statement()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .execute(db());
    }


}
