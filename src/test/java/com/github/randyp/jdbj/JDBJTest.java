package com.github.randyp.jdbj;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;

import static org.junit.Assert.*;

public class JDBJTest extends StudentTest {

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
            assertTrue(Student.selectAll.execute(connection).isEmpty());
            //noinspection AccessStaticViaInstance
            new JDBJ().path(tmpFile.toPath()).statement().execute(connection); //for test coverage
            assertEquals(1, Student.selectAll.execute(connection).size());
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
