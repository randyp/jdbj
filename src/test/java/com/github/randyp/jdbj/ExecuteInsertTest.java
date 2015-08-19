package com.github.randyp.jdbj;

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
                .bindString(":first_name", newStudent.firstName)
                .bindString(":last_name", newStudent.lastName)
                .bindBigDecimal(":gpa", newStudent.gpa);

        final List<Long> keys = insertQuery.execute(db);
        final List<Student> actual = Student.selectAll.execute(db);

        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        //noinspection AccessStaticViaInstance
        final ExecuteInsert<Long> executeUpdate = new JDBJ().insert("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)", rs -> rs.getLong(1)) //for test coverage...
                .bindString(":s", "3.1");
        assertEquals(1, executeUpdate.execute(db).size());
    }
}
