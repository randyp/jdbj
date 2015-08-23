package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.lambda.ResultMapper;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteInsertTest extends StudentTest {

    @Test
    public void insertOne() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert).insert(keyMapper)
                .bind(newStudent);

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.selectAll.execute(db());

        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void insertOneUsingLambda() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert).insert(keyMapper)
                .bind(newStudent::bindings);

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.selectAll.execute(db());

        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        //noinspection AccessStaticViaInstance
        final ExecuteInsert<Long> executeUpdate = JDBJ.insert("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)", rs -> rs.getLong(1)) //for test coverage...
                .bindString(":s", "3.1");
        assertEquals(1, executeUpdate.execute(db()).size());
    }
}
