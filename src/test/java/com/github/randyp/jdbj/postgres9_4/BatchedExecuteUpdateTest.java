package com.github.randyp.jdbj.postgres9_4;

import com.github.randyp.jdbj.BatchedExecuteUpdate;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.Student;
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
                    .bindLong(":id", student.getId())
                    .bindString(":first_name", student.getFirstName())
                    .bindString(":last_name", student.getLastName())
                    .bindBigDecimal(":gpa", student.getGpa())
                    .endBatch();
        }

        final List<Student> actual;
        try (Connection connection = db.getConnection()) {
            assertArrayEquals(new int[]{1, 1}, insertQuery.execute(connection));
            actual = Student.selectAll.execute(connection);
        }
        assertEquals(expected, actual);
    }
}
