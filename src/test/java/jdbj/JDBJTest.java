package jdbj;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class JDBJTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    public static class Query {
        @Test
        public void selectRunExecute() throws Exception {
            final String[] firstColumnName = {null};
            final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES")
                    .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1));

            try (Connection connection = db.getConnection()) {
                query.execute(connection);
            }

            assertEquals("TABLE_CATALOG", firstColumnName[0]);
        }

        @Test
        public void selectRunBindValueExecute() throws Exception {
            final int[] count = {0};
            final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id")
                    .runnable(rs -> {
                        while (rs.next()) {
                            count[0]++;
                        }
                    })
                    .bindLong(":id", -28);

            try (Connection connection = db.getConnection()) {
                query.execute(connection);
            }

            assertEquals(1, count[0]);
        }

        @Test
        public void selectRunBindStringListExecute() throws Exception {
            final int[] count = {0};
            final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA in :schemas")
                    .runnable(rs -> {
                        while (rs.next()) {
                            count[0]++;
                        }
                    })
                    .bindStrings(":schemas", "INFORMATION_SCHEMA");

            try (Connection connection = db.getConnection()) {
                query.execute(connection);
            }

            assertEquals(29, count[0]);
        }

        @Test
        public void selectMapToListExecute() throws Exception {
            final ExecuteQuery<List<String>> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = -28")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .toList();

            final List<String> results;
            try (Connection connection = db.getConnection()) {
                results = query.execute(connection);
            }

            assertEquals(Collections.singletonList("SESSION_STATE"), results);
        }

        @Test
        public void selectMapToListBindLongsExecute() throws Exception {
            //noinspection RedundantArrayCreation
            final ExecuteQuery<List<String>> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .toList()
                    .bindLongs(":ids", new long[]{-28L});

            final List<String> results;
            try (Connection connection = db.getConnection()) {
                results = query.execute(connection);
            }

            assertEquals(Collections.singletonList("SESSION_STATE"), results);
        }

        @Test
        public void selectMapFirstBindLongsExecute() throws Exception {
            //noinspection RedundantArrayCreation
            final ExecuteQuery<String> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .first()
                    .bindLongs(":ids", new long[]{-28L});

            final String result;
            try (Connection connection = db.getConnection()) {
                result = query.execute(connection);
            }

            assertEquals("SESSION_STATE", result);
        }

        @Test
        public void selectMapRemapFirstBindLongsExecute() throws Exception {
            //noinspection RedundantArrayCreation
            final ExecuteQuery<Integer> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .remap(String::length)
                    .first()
                    .bindLongs(":ids", new long[]{-28L});

            final Integer result;
            try (Connection connection = db.getConnection()) {
                result = query.execute(connection);
            }

            assertNotNull(result);
            assertEquals("SESSION_STATE".length(), result.intValue());
        }

        @Test
        public void selectMapBindLongsStreamExecute() throws Exception {
            //noinspection RedundantArrayCreation
            final StreamQuery<String> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .stream()
                    .bindLongs(":ids", new long[]{-28L});

            final Optional<String> result;
            try (Connection connection = db.getConnection()) {
                try (Stream<String> stream = query.execute(connection)) {
                    result = stream.findFirst();
                }
            }

            assertTrue(result.isPresent());
            assertEquals("SESSION_STATE", result.get());
        }

        @Test
        public void query() throws Exception {
            final ExecuteQuery<String> query = JDBJ.query("tables_by_schema.sql")
                    .map(rs -> rs.getString("TABLE_NAME"))
                    .first()
                    .bindString(":table_schema", "INFORMATION_SCHEMA");

            final String result;
            try (Connection connection = db.getConnection()) {
                result = query.execute(connection);
            }

            assertNotNull(result);
        }

        @Test(expected = RuntimeException.class)
        public void queryResourceNotFound() throws Exception {
            //noinspection AccessStaticViaInstance
            new JDBJ().query("tables_by_froozgaba.sql");
        }
    }

    public static class Insert {

        @ClassRule
        public static TestRule create_table = Student.createTableRule;

        @After
        public void tearDown() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
                ps.execute();
            }
        }

        @Test(expected = IllegalStateException.class)
        public void insertQueryNotEnoughBindings() throws Exception {
            final InsertQuery insertQuery = JDBJ.insertQueryString(Student.insert);
            try (Connection connection = db.getConnection()) {
                insertQuery.execute(connection);
            }
        }

        @Test
        public void insertOne() throws Exception {
            final Student expected = new Student(10L, "Ada", "Dada", new BigDecimal("3.1"));
            final InsertQuery insertQuery = JDBJ.insertQueryString(Student.insert)
                    .bindLong(":id", expected.id)
                    .bindString(":first_name", expected.firstName)
                    .bindString(":last_name", expected.lastName)
                    .bindBigDecimal(":gpa", expected.gpa);

            final List<Student> actual;
            try (Connection connection = db.getConnection()) {
                assertEquals(1, insertQuery.execute(connection));
                actual = Student.selectAll.execute(connection);
            }
            assertEquals(Collections.singletonList(expected), actual);
        }
    }

    public static class BatchInsert {

        @ClassRule
        public static TestRule create_table = Student.createTableRule;

        @After
        public void tearDown() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
                ps.execute();
            }
        }

        @Test
        public void insertBatch() throws Exception {
            final List<Student> expected = Arrays.asList(
                    new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1")),
                    new Student(11L, "Ada11", "Dada11", new BigDecimal("3.2"))
            );

            BatchedInsertQuery insertQuery = JDBJ.insertQueryString(Student.insert).asBatch();
            for (Student student : expected) {
                insertQuery.startBatch()
                        .bindLong(":id", student.id)
                        .bindString(":first_name", student.firstName)
                        .bindString(":last_name", student.lastName)
                        .bindBigDecimal(":gpa", student.gpa)
                        .endBatch();
            }

            final List<Student> actual;
            try (Connection connection = db.getConnection()) {
                assertArrayEquals(new int[]{1, 1}, insertQuery.execute(connection));
                actual = Student.selectAll.execute(connection);
            }
            assertEquals(expected, actual);
        }

        @Test(expected = IllegalStateException.class)
        public void noBatchesAdded() throws Exception {
            BatchedInsertQuery insertQuery = JDBJ.insertQueryString(Student.insert).asBatch();

            try (Connection connection = db.getConnection()) {
                insertQuery.execute(connection);
            }
        }

        @Test(expected = IllegalStateException.class)
        public void missingBindings() throws Exception {
            final Student student = new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1"));

            JDBJ.insertQueryString(Student.insert).asBatch()
                    .startBatch()
                    .bindLong(":id", student.id)
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .endBatch();
        }
    }


    private static class Student {

        public static final TestRule createTableRule = (base, description) -> new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String createStudents = "CREATE TABLE student(id BIGINT, first_name varchar, last_name varchar, gpa varchar)";
                try (Connection connection = db.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(createStudents)) {
                    preparedStatement.execute();
                }
                base.evaluate();
                final String dropStudents = "DROP TABLE student";
                try (Connection connection = db.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(dropStudents)) {
                    preparedStatement.execute();
                }
            }
        };

        static final String insert = "INSERT INTO student(id, first_name, last_name, gpa) VALUES(:id, :first_name, :last_name, :gpa)";

        static final ExecuteQuery<List<Student>> selectAll = JDBJ.queryString("SELECT * FROM student ORDER BY id ASC")
                .map(Student::from)
                .toList();

        static Student from(ResultSet rs) throws SQLException {
            return new Student(rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getBigDecimal("gpa"));
        }

        long id;
        String firstName;
        String lastName;
        BigDecimal gpa;

        public Student(long id, String firstName, String lastName, BigDecimal gpa) {
            if (firstName == null) {
                throw new IllegalArgumentException();
            }
            if (lastName == null) {
                throw new IllegalArgumentException();
            }
            if (gpa == null) {
                throw new IllegalArgumentException();
            }
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gpa = gpa;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Student student = (Student) o;

            return id == student.id &&
                    firstName.equals(student.firstName) &&
                    lastName.equals(student.lastName) &&
                    gpa.equals(student.gpa);

        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + firstName.hashCode();
            result = 31 * result + lastName.hashCode();
            result = 31 * result + gpa.hashCode();
            return result;
        }
    }
}
