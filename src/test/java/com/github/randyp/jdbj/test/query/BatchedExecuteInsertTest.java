package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.lambda.ResultMapper;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class BatchedExecuteInsertTest extends StudentTest {

    @Test
    public void insertBatch() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT).insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindValues(newStudent::bindings)
                .addBatch();

        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }

    @Test
    public void insertBatches() throws Exception {
        final List<NewStudent> newStudents = Arrays.asList(
                new NewStudent("Ada10", "Dada10", new BigDecimal("3.1")),
                new NewStudent("Ada11", "Dada11", new BigDecimal("3.1"))
        );

        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT).insert(keyMapper)
                .asBatch();
        for (NewStudent newStudent : newStudents) {
            insertQuery.startBatch()
                    .bindValues(newStudent::bindings)
                    .addBatch();
        }


        final List<Long> keys = insertQuery.execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(2, keys.size());
        final List<Student> expected = Arrays.asList(
                newStudents.get(0).withId(keys.get(0)),
                newStudents.get(1).withId(keys.get(1))
        );
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void noBatchesAdded() throws Exception {
        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        BatchedExecuteInsert<Long> insertQuery = JDBJ.resource(Student.INSERT)
                .insert(keyMapper)
                .asBatch();

        insertQuery.execute(db());
    }

    @Test(expected = IllegalStateException.class)
    public void missingBindings() throws Exception {
        final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        ResultMapper<Long> keyMapper = rs -> rs.getLong(1);
        JDBJ.resource(Student.INSERT)
                .insert(keyMapper)
                .asBatch()
                .startBatch()
                .bindString(":first_name", student.getFirstName())
                .bindString(":last_name", student.getLastName())
                .addBatch();
    }

    @Test
    public void batchAddedMultipleTimes() throws Exception {
        final NewStudent newStudent = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

        final BatchedExecuteInsert<Long> batchInsert = JDBJ.resource(Student.INSERT)
                .insert(rs -> rs.getLong(1))
                .asBatch();
        final BatchedExecuteInsert<Long>.Batch batch = batchInsert
                .startBatch()
                .bindValues(newStudent::bindings);

        batch.addBatch();
        batch.addBatch();
        batchInsert.execute(db());
        assertEquals(2, Student.SELECT_ALL.execute(db()).size());
    }
}
