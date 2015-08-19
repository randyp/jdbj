package com.github.randyp.jdbj.postgres9_4;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExecuteInsertTest extends StudentTest {

    @Test
    public void insertOne() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert).insert(keyMapper)
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa());

        final List<Long> keys;
        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            keys = insertQuery.execute(connection);
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        //noinspection AccessStaticViaInstance
        final ExecuteInsert<Long> executeUpdate = JDBJ.insert("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)", rs -> rs.getLong(1))
                .bindString(":s", "3.1");
        try (Connection connection = db.getConnection()) {
            assertEquals(1, executeUpdate.execute(connection).size());
        }
    }

}
