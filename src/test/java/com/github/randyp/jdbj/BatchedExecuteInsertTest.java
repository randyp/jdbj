package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BatchedExecuteInsertTest extends StudentTest {

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void insertBatch() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        //noinspection deprecation
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert).insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindString(":first_name", newStudent.firstName)
                .bindString(":last_name", newStudent.lastName)
                .bindBigDecimal(":gpa", newStudent.gpa)
                .endBatch();

        final List<Long> keys;
        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            keys = insertQuery.execute(connection);
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test(expected = IllegalStateException.class)
    public void noBatchesAdded() throws Exception {
        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        //noinspection deprecation
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert)
                .insert(keyMapper)
                .asBatch();

        try (Connection connection = db.getConnection()) {
            insertQuery.execute(connection);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        JDBJ.resource(Student.insert)
                .insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.firstName)
                .bindString(":last_name", student.lastName)
                .endBatch();
    }

    @Test(expected = IllegalStateException.class)
    public void batchAlreadyEnded() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        //noinspection deprecation
        final BatchedExecuteInsert<Long>.Batch batch = JDBJ.resource(Student.insert)
                .insert(rs -> rs.getLong(1))
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.firstName)
                .bindString(":last_name", student.lastName)
                .bindBigDecimal(":gpa", student.gpa);

        batch.endBatch();
        batch.bindString(":first_name", student.firstName);
    }

}
