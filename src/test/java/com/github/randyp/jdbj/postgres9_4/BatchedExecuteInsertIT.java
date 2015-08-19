package com.github.randyp.jdbj.postgres9_4;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BatchedExecuteInsertIT extends StudentTest {

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

        final List<Long> keys;
        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            keys = insertQuery.execute(connection);
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(1, keys.size());
        assertEquals(Collections.singletonList(newStudent.withId(keys.get(0))), actual);
    }
}
