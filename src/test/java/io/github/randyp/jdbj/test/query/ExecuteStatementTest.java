package io.github.randyp.jdbj.test.query;

import io.github.randyp.jdbj.ExecuteStatement;
import io.github.randyp.jdbj.JDBJ;
import io.github.randyp.jdbj.student.Student;
import io.github.randyp.jdbj.student.NewStudent;
import io.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteStatementTest extends StudentTest {

    @Test
    public void insertOne() throws Exception {
        JDBJ.resource("student_insert_ada10.sql").statement().execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(1, actual.size());
        assertEquals("Ada10", actual.get(0).getFirstName());
    }

    @Test
    public void convenienceMethodOnJDBJ() throws Exception {
        final ExecuteStatement executeUpdate = JDBJ.statement("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        executeUpdate.execute(db());
        assertEquals(1, Student.SELECT_ALL.execute(db()).size());
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));
        JDBJ.resource(NewStudent.INSERT).statement()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .execute(db());
    }


}
