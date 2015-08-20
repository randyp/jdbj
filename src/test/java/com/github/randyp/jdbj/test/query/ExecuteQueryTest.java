package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.ExecuteQuery;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.StreamQuery;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ExecuteQueryTest extends StudentTest {

    private final List<NewStudent> newStudents = Arrays.asList(
            new NewStudent("Ada10", "Dada10", new BigDecimal("3.9")),
            new NewStudent("Ada11", "Dada11", new BigDecimal("4.9"))
    );

    @Before
    public void setUp() throws Exception {

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
    public void selectMapToListExecute() throws Exception {
        final ExecuteQuery<List<Student>> query = JDBJ.query("SELECT * FROM student ORDER BY id")
                .map(Student::from)
                .toList();
        final List<Student> results = query.execute(db());
        assertEquals(2, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(newStudents.get(i).getFirstName(), results.get(i).getFirstName());
        }
    }

    @Test
    public void selectMapToListBindListExecute() throws Exception {
        final ExecuteQuery<List<Student>> query = JDBJ.query("SELECT * FROM student WHERE last_name in :last_names")
                .map(Student::from)
                .toList()
                .bindStrings(":last_names", "Dada11");

        final List<Student> results = query.execute(db());
        assertEquals(1, results.size());
        assertEquals("Ada11", results.get(0).getFirstName());
    }

    @Test
    public void selectMapFirstBindListExecute() throws Exception {
        final ExecuteQuery<Optional<Student>> query = JDBJ.query("SELECT * FROM student WHERE last_name in :last_names")
                .map(Student::from)
                .first()
                .bindStrings(":last_names", "Dada11");

        final Optional<Student> result = query.execute(db());
        assertTrue(result.isPresent());
        assertEquals("Ada11", result.get().getFirstName());
    }

    @Test
    public void selectMapRemapFirstBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<Optional<String>> query = JDBJ.query("SELECT * FROM student WHERE last_name in :last_names")
                .map(Student::from)
                .remap(NewStudent::getFirstName)
                .first()
                .bindStrings(":last_names", "Dada11");

        final Optional<String> result = query.execute(db());
        assertTrue(result.isPresent());
        assertEquals("Ada11", result.get());
    }

    @Test
    public void selectMapBindLongsStreamExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final StreamQuery<Student> query = JDBJ.query("SELECT * FROM student WHERE last_name in :last_names")
                .map(Student::from)
                .bindStrings(":last_names", "Dada11")
                .toStream();

        final Optional<Student> result;
        try (Stream<Student> stream = query.execute(db())) {
            result = stream.findFirst();
        }

        assertTrue(result.isPresent());
        assertEquals("Ada11", result.get().getFirstName());
    }

    @Test
    public void query() throws Exception {
        final ExecuteQuery<Optional<Student>> query = JDBJ.resource("student_all_ordered_by_id.sql")
                .query()
                .map(Student::from)
                .first();
        final Optional<Student> result = query.execute(db());
        assertTrue(result.isPresent());
    }
}
