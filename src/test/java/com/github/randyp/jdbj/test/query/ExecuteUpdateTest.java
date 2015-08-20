package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.ExecuteUpdate;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteUpdateTest extends StudentTest {

    @Test(expected = IllegalStateException.class)
    public void insertQueryNotEnoughBindings() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert).update();
        try (Connection connection = db().getConnection()) {
            executeUpdate.execute(connection);
        }
    }

    @Test
    public void insertOne() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert).update()
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa());

        assertEquals(1, executeUpdate.execute(db()));
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(1, actual.size());
        assertEquals(newStudent.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void convenienceOnJDBJ() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.update("INSERT INTO student (first_name, last_name, gpa) VALUES (:s, :s, :s)")
                .bindString(":s", "3.1");
        assertEquals(1, executeUpdate.execute(db()));
    }

}
