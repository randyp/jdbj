package io.github.randyp.jdbj.test.query;

import io.github.randyp.jdbj.ExecuteInsert;
import io.github.randyp.jdbj.JDBJ;
import io.github.randyp.jdbj.PositionalBindings;
import io.github.randyp.jdbj.lambda.ResultMapper;
import io.github.randyp.jdbj.student.NewStudent;
import io.github.randyp.jdbj.student.Student;
import io.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteInsertTest extends StudentTest {

    @Test
     public void insertOneUsingValueBindingsSupplier() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT).insert(keyMapper)
                .bindValues(newStudent::bindings);

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());

        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void insertOneUsingPositionalBindingsSupplier() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT).insert(keyMapper)
                .bind(() -> new PositionalBindings().addAll(newStudent.bindings()));

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());

        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void insertOneLonghand() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        final ExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT).insert(keyMapper)
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa());

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());

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
