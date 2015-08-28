package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ExecuteScriptTest extends StudentTest {

    @Test
    public void insertTwoNoParams() throws Exception {
        JDBJ.resource("student_insert_ada10_ada11.sql").script().execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(2, actual.size());
        actual.sort(Student.Compare.FIRST_NAME);
        assertEquals("Ada10", actual.get(0).getFirstName());
        assertEquals("Ada11", actual.get(1).getFirstName());
    }

    @Test
    public void insertTwoParameters() throws Exception {
        final String ada10 = "Ada10";
        final String ada11 = "Ada11";

        JDBJ.script(
                "INSERT INTO student(first_name, last_name, gpa) VALUES(:name0, 'Dada10', '3.1');\n" +
                        "INSERT INTO student(first_name, last_name, gpa) VALUES(:name1, 'Dada11', '3.2');"
        )
                .bindString(":name0", ada10)
                .bindString(":name1", ada11)
                .execute(db());
        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(2, actual.size());
        actual.sort(Student.Compare.FIRST_NAME);
        assertEquals("Ada10", actual.get(0).getFirstName());
        assertEquals("Ada11", actual.get(1).getFirstName());
    }

    @Test
    public void convenienceMethodOnJDBJ() throws Exception {
        JDBJ.script("INSERT INTO student(first_name, last_name, gpa) VALUES ('a', 'b', '3.1');")
                .execute(db());
        assertEquals(1, Student.SELECT_ALL.execute(db()).size());
    }
}
