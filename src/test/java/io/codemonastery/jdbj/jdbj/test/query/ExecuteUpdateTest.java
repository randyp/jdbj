package io.codemonastery.jdbj.jdbj.test.query;

import io.codemonastery.jdbj.jdbj.ExecuteUpdate;
import io.codemonastery.jdbj.jdbj.JDBJ;
import io.codemonastery.jdbj.jdbj.student.Student;
import io.codemonastery.jdbj.jdbj.student.NewStudent;
import io.codemonastery.jdbj.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteUpdateTest extends StudentTest {

    @Test(expected = IllegalStateException.class)
    public void insertQueryNotEnoughBindings() throws Exception {
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.INSERT).update();
        try (Connection connection = db().getConnection()) {
            executeUpdate.execute(connection);
        }
    }

    @Test
    public void insertOne() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
        final ExecuteUpdate executeUpdate = JDBJ.resource(Student.INSERT).update()
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa());

        assertEquals(1, executeUpdate.execute(db()));
        final List<Student> actual = Student.SELECT_ALL.execute(db());
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
