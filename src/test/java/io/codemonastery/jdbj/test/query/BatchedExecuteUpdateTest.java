package io.codemonastery.jdbj.test.query;

import io.codemonastery.jdbj.BatchedExecuteUpdate;
import io.codemonastery.jdbj.JDBJ;
import io.codemonastery.jdbj.student.NewStudent;
import io.codemonastery.jdbj.student.Student;
import io.codemonastery.jdbj.student.StudentTest;
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

        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.INSERT).update().asBatch();
        for (NewStudent newStudent : expected) {
            insertQuery.startBatch()
                    .bindValues(newStudent::bindings)
                    .addBatch();
        }

        assertArrayEquals(new int[]{1, 1}, insertQuery.execute(db()));
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            final NewStudent expectedStudent = expected.get(i);
            final Student actualStudent = actual.get(i);
            assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void noBatchesAdded() throws Exception {
        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.INSERT)
                .update()
                .asBatch();

        try (Connection connection = db().getConnection()) {
            insertQuery.execute(connection);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        JDBJ.resource(Student.INSERT)
                .update()
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .addBatch();
    }

    @Test
    public void batchAddedMultipleTimes() throws Exception {
        final NewStudent newStudent = new NewStudent( "Ada10", "Dada10", new BigDecimal("3.1"));

        final BatchedExecuteUpdate batchUpdate = JDBJ.resource(Student.INSERT)
                .update()
                .asBatch();
        final BatchedExecuteUpdate.Batch batch = batchUpdate
                .startBatch()
                .bindValues(newStudent::bindings);

        batch.addBatch();
        batch.addBatch();
        batchUpdate.execute(db());
        assertEquals(2, Student.SELECT_ALL.execute(db()).size());
    }
}
