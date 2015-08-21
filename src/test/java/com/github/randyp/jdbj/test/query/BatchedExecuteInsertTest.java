package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.lambda.ResultSetMapper;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class BatchedExecuteInsertTest extends StudentTest {

    //can only test with one batch, as keys are only available from last batch in H2
    @Test
    public void insertBatch() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        //noinspection deprecation
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.insert).insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa())
                .endBatch();

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.selectAll.execute(db());
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

        insertQuery.execute(db());
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultSetMapper<Long> keyMapper = rs -> rs.getLong(1);
        JDBJ.resource(Student.insert)
                .insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
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
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .bindBigDecimal(":gpa", student.getGpa());

        batch.endBatch();
        batch.bindString(":first_name", student.getFirstName());
    }
}
