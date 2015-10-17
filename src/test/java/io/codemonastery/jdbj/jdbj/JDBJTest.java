package io.codemonastery.jdbj.jdbj;

import io.codemonastery.jdbj.jdbj.db.h2_1_4.H2Rule;
import io.codemonastery.jdbj.jdbj.student.Student;
import io.codemonastery.jdbj.jdbj.student.StudentTest;
import org.junit.ClassRule;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.Optional;

import static org.junit.Assert.*;

public class JDBJTest extends StudentTest {

    @ClassRule
    public static final H2Rule db = dbRule();

    @Override
    public DataSource db() {
        return db;
    }


    @Test
    public void string_string() throws Exception {
        final Optional<Student> noStudent = JDBJ.string("SELECT *").string("FROM student")
                .query().map(Student::from).first().execute(db);
        assertFalse(noStudent.isPresent());
    }

    @Test
    public void reader_reader() throws Exception {
        final Optional<Student> noStudent = JDBJ.reader(() -> new StringReader("SELECT *"))
                .reader(()->new StringReader("FROM student"))
                .query().map(Student::from).first().execute(db);
        assertFalse(noStudent.isPresent());
    }

    @Test
    public void string_resource() throws Exception {
        assertTrue(Student.SELECT_ALL.execute(db).isEmpty());
        JDBJ.string("")
                .resource("student_insert_ada10.sql")
                .update()
                .execute(db);
        assertFalse(Student.SELECT_ALL.execute(db).isEmpty());
    }

    @Test
    public void stream_stream() throws Exception {
        assertTrue(Student.SELECT_ALL.execute(db).isEmpty());
        final URL url = JDBJTest.class.getClassLoader().getResource("student_insert_ada10.sql");
        assertNotNull(url);
        JDBJ.stream(url::openStream)
                .string(";")
                .stream(url::openStream)
                .update()
                .execute(db);
        assertFalse(Student.SELECT_ALL.execute(db).isEmpty());
    }

    @Test
    public void path() throws Exception {
        final File tmpFile = File.createTempFile("JDBJTest_student_insert", ".sql");
        tmpFile.deleteOnExit();

        final URL url = JDBJTest.class.getClassLoader().getResource("student_insert_ada10.sql");
        assertNotNull(url);

        try (InputStream in = url.openStream()) {
            Files.copy(in, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try(Connection connection = db.getConnection()){
            assertTrue(Student.SELECT_ALL.execute(connection).isEmpty());
            //noinspection AccessStaticViaInstance
            new JDBJ().path(tmpFile.toPath()).statement().execute(connection); //for test coverage on constructor
            assertEquals(1, Student.SELECT_ALL.execute(connection).size());
        }
    }

    @Test
    public void string_path() throws Exception {
        final File tmpFile = File.createTempFile("JDBJTest_student_insert", ".sql");
        tmpFile.deleteOnExit();

        final URL url = JDBJTest.class.getClassLoader().getResource("student_insert_ada10.sql");
        assertNotNull(url);

        try (InputStream in = url.openStream()) {
            Files.copy(in, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try(Connection connection = db.getConnection()){
            assertTrue(Student.SELECT_ALL.execute(connection).isEmpty());
            //noinspection AccessStaticViaInstance
            JDBJ.string("").path(tmpFile.toPath()).statement().execute(connection); //for test coverage on constructor
            assertEquals(1, Student.SELECT_ALL.execute(connection).size());
        }
    }

    @Test(expected = RuntimeException.class)
    public void queryResourceNotFound() throws Exception {
        //noinspection AccessStaticViaInstance
        JDBJ.resource("tables_by_froozgaba.sql");
    }

    @Test
    public void exceptionDuringSupply() throws Exception {
        try{
            JDBJ.reader(()->{
                throw new IOException("did I do that?");
            });
            fail("expected steve urkel exception");
        }catch (RuntimeException e){
            assertNotNull(e.getCause());
            assertEquals("did I do that?", e.getCause().getMessage());
        }
    }
}
