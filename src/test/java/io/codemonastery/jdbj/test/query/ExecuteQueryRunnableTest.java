package io.codemonastery.jdbj.test.query;

import io.codemonastery.jdbj.JDBJ;
import io.codemonastery.jdbj.student.NewStudent;
import io.codemonastery.jdbj.student.StudentTest;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ExecuteQueryRunnableTest extends StudentTest {

    @Before
    public void setUp() throws Exception {
        final List<NewStudent> newStudents = Arrays.asList(
                new NewStudent("Ada10", "Dada10", new BigDecimal("3.9")),
                new NewStudent("Ada11", "Dada11", new BigDecimal("4.9"))
        );
        try(Connection connection = db().getConnection();
            PreparedStatement ps =connection.prepareStatement("INSERT INTO student(first_name, last_name, gpa) VALUES (?, ?, ?)")){
            for (NewStudent newStudent : newStudents) {
                ps.setString(1, newStudent.getFirstName());
                ps.setString(2, newStudent.getLastName());
                ps.setBigDecimal(3, newStudent.getGpa());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Test
    public void selectRunExecute() throws Exception {
        final String[] firstColumnName = {null};
        JDBJ.string("SELECT first_name FROM student")
                .query()
                .run(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1).toLowerCase())
                .execute(db());

        assertEquals("first_name", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT * FROM student where first_name = :name")
                .query()
                .run(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindString(":name", "Ada10")
                .execute(db());

        assertEquals(1, count[0]);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT * FROM student where last_name in :last_names")
                .query()
                .bindStrings(":last_names", "Dada10", "Dada11")
                .run(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .execute(db());

        assertTrue(count[0] > 1);
    }
}
