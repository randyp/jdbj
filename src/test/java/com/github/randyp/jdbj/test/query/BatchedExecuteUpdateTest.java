package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.BatchedExecuteUpdate;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class BatchedExecuteUpdateTest extends StudentTest {

    @Test
    public void insertBatch() throws Exception {
        final List<NewStudent> expected = Arrays.asList(
                new NewStudent("Ada10", "Dada10", new BigDecimal("3.1")),
                new NewStudent("Ada11", "Dada11", new BigDecimal("3.2"))
        );

        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.insert).update().asBatch();
        for (NewStudent student : expected) {
            insertQuery.startBatch()
                    .bindString(":first_name", student.getFirstName())
                    .bindString(":last_name", student.getLastName())
                    .bindBigDecimal(":gpa", student.getGpa())
                    .endBatch();
        }

        assertArrayEquals(new int[]{1, 1}, insertQuery.execute(db()));
        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            final NewStudent expectedStudent = expected.get(i);
            final Student actualStudent = actual.get(i);
            assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void noBatchesAdded() throws Exception {
        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.insert)
                .update()
                .asBatch();

        try (Connection connection = db().getConnection()) {
            insertQuery.execute(connection);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        JDBJ.resource(Student.insert)
                .update()
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .endBatch();
    }

    @Test(expected = IllegalStateException.class)
    public void batchAlreadyEnded() throws Exception {
        final NewStudent newStudent = new NewStudent( "Ada10", "Dada10", new BigDecimal("3.1"));

        final BatchedExecuteUpdate.Batch batch = JDBJ.resource(Student.insert)
                .update()
                .asBatch()
                .startBatch()
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa());

        batch.endBatch();
        batch.bindString(":first_name", newStudent.getFirstName());
    }
}
