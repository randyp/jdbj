package com.github.randyp.jdbj;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BatchedExecuteUpdateTest extends StudentTest {

    @Test
    public void insertBatch() throws Exception {
        final List<Student> expected = Arrays.asList(
                new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1")),
                new Student(11L, "Ada11", "Dada11", new BigDecimal("3.2"))
        );

        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.insert_id).update().asBatch();
        for (Student student : expected) {
            insertQuery.startBatch()
                    .bindLong(":id", student.id)
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .bindBigDecimal(":gpa", student.gpa)
                    .endBatch();
        }

        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            assertArrayEquals(new int[]{1, 1}, insertQuery.execute(connection));
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void noBatchesAdded() throws Exception {
        BatchedExecuteUpdate insertQuery = JDBJ.resource(Student.insert_id)
                .update()
                .asBatch();

        try (Connection connection = db.getConnection()) {
            insertQuery.execute(connection);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final Student student = new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1"));

        JDBJ.resource(Student.insert_id)
                .update()
                .asBatch()
                .startBatch()
                .bindLong(":id", student.id)
                .bindString(":first_name", student.firstName)
                .bindString(":last_name", student.lastName)
                .endBatch();
    }

    @Test(expected = IllegalStateException.class)
    public void batchAlreadyEnded() throws Exception {
        final Student student = new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1"));

        final BatchedExecuteUpdate.Batch batch = JDBJ.resource(Student.insert_id)
                .update()
                .asBatch()
                .startBatch()
                .bindLong(":id", student.id)
                .bindString(":first_name", student.firstName)
                .bindString(":last_name", student.lastName)
                .bindBigDecimal(":gpa", student.gpa);

        batch.endBatch();
        batch.bindLong(":id", student.id);
    }

}
