package io.github.randyp.jdbj.example;

import io.github.randyp.jdbj.db.h2_1_4.H2Rule;
import io.github.randyp.jdbj.student.NewStudent;
import io.github.randyp.jdbj.student.Student;
import io.github.randyp.jdbj.student.StudentTest;
import org.junit.ClassRule;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StudentDAOTest extends StudentTest {

    @ClassRule
    public static final H2Rule db = dbRule();

    @Override
    public DataSource db() {
        return db;
    }

    @Test
     public void insertOneSelectOne() throws Exception {
        try(Connection connection = db.getConnection()){
            final StudentDAO dao = new StudentDAO(connection);
            assertTrue(dao.all().isEmpty());
            final Student inserted = dao.insert(new NewStudent("Ada", "Java", new BigDecimal("4.1")));
            final Optional<Student> selected = dao.all().stream().findFirst();
            assertTrue(selected.isPresent());

            assertEquals(inserted, selected.get());
        }
    }

    @Test
    public void insertCoupleSelectCouple() throws Exception {
        try(Connection connection = db.getConnection()){
            final StudentDAO dao = new StudentDAO(connection);
            assertTrue(dao.all().isEmpty());

            final List<Student> inserted = Arrays.asList(
                    dao.insert(new NewStudent("Ada", "Java", new BigDecimal("4.1"))),
                    dao.insert(new NewStudent("Bada", "Bing", new BigDecimal("2.1")))
            );
            final List<Student> selected = dao.all();
            assertEquals(inserted, selected);
        }
    }

    @Test
     public void selectById() throws Exception {
        try(Connection connection = db.getConnection()){
            final StudentDAO dao = new StudentDAO(connection);
            assertTrue(dao.all().isEmpty());

            final Student ada = dao.insert(new NewStudent("Ada", "Java", new BigDecimal("4.1")));
            dao.insert(new NewStudent("Bada", "Bing", new BigDecimal("2.1")));
            final Optional<Student> adaById = dao.byId(ada.getId());
            assertTrue(adaById.isPresent());
            assertEquals(ada, adaById.get());
        }
    }

    @Test
    public void selectByIds() throws Exception {
        try(Connection connection = db.getConnection()){
            final StudentDAO dao = new StudentDAO(connection);
            assertTrue(dao.all().isEmpty());

            final Student ada = dao.insert(new NewStudent("Ada", "Java", new BigDecimal("4.1")));
            dao.insert(new NewStudent("Bada", "Bing", new BigDecimal("2.1")));
            final List<Student> students = dao.byIds(new long[]{ada.getId()});
            assertEquals(Collections.singletonList(ada), students);
        }
    }
}
