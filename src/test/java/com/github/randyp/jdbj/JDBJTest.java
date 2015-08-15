package com.github.randyp.jdbj;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import javax.sql.DataSource;
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

    public static class Update {

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
            final UpdateQuery updateQuery = JDBJ.insertQuery(Student.insert_id);
            try (Connection connection = db.getConnection()) {
                updateQuery.execute(connection);
            }
        }

        @Test
        public void insertOne() throws Exception {
            final Student expected = new Student(10L, "Ada", "Dada", new BigDecimal("3.1"));
            final UpdateQuery updateQuery = JDBJ.insertQuery(Student.insert_id)
                    .bindLong(":id", expected.id)
                    .bindString(":first_name", expected.firstName)
                    .bindString(":last_name", expected.lastName)
                    .bindBigDecimal(":gpa", expected.gpa);

            final List<Student> actual;
            try (Connection connection = db.getConnection()) {
                assertEquals(1, updateQuery.execute(connection));
                actual = Student.selectAll.execute(connection);
            }
            assertEquals(Collections.singletonList(expected), actual);
        }
    }

    public static class BatchUpdate {

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

            BatchedUpdateQuery insertQuery = JDBJ.insertQuery(Student.insert_id).asBatch();
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
            BatchedUpdateQuery insertQuery = JDBJ.insertQuery(Student.insert_id).asBatch();

            try (Connection connection = db.getConnection()) {
                insertQuery.execute(connection);
            }
        }

        @Test(expected = IllegalStateException.class)
        public void missingBindings() throws Exception {
            final Student student = new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1"));

            JDBJ.insertQuery(Student.insert_id).asBatch()
                    .startBatch()
                    .bindLong(":id", student.id)
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .endBatch();
        }

        @Test(expected = IllegalStateException.class)
        public void batchAlreadyEnded() throws Exception {
            final Student student = new Student(10L, "Ada10", "Dada10", new BigDecimal("3.1"));

            final BatchedUpdateQuery.Batch batch = JDBJ.insertQuery(Student.insert_id).asBatch()
                    .startBatch()
                    .bindLong(":id", student.id)
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .bindBigDecimal(":gpa", student.gpa);

            batch.endBatch();
            batch.bindLong(":id", student.id);
        }
    }

    public static class InsertReturnKeys {

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
        public void insertOne() throws Exception {
            final NewStudent newStudent = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));
            final InsertReturnKeysQuery<Long> insertQuery = JDBJ.insertQueryGetKeys(Student.insert, rs -> rs.getLong(1))
                    .bindString(":first_name", newStudent.firstName)
                    .bindString(":last_name", newStudent.lastName)
                    .bindBigDecimal(":gpa", newStudent.gpa);

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

    public static class BatchedInsertReturnKeys {

        @ClassRule
        public static TestRule create_table = Student.createTableRule;

        @After
        public void tearDown() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
                ps.execute();
            }
        }

        //can only test with one batch, as keys are only available from last batch in H2
        @Test
        public void insertBatch() throws Exception {
            final NewStudent newStudent = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

            //noinspection deprecation
            BatchedInsertReturnKeysQuery<Long> insertQuery = JDBJ.insertQueryGetKeys(Student.insert, rs -> rs.getLong(1))
                    .asBatch()
                    .startBatch()
                    .bindString(":first_name", newStudent.firstName)
                    .bindString(":last_name", newStudent.lastName)
                    .bindBigDecimal(":gpa", newStudent.gpa)
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

        @Test(expected = IllegalStateException.class)
        public void noBatchesAdded() throws Exception {
            //noinspection deprecation
            BatchedInsertReturnKeysQuery<Long> insertQuery = JDBJ.insertQueryGetKeys(Student.insert, rs -> rs.getLong(1)).asBatch();

            try (Connection connection = db.getConnection()) {
                insertQuery.execute(connection);
            }
        }

        @Test(expected = IllegalStateException.class)
        public void missingBindings() throws Exception {
            final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

            JDBJ.insertQueryGetKeys(Student.insert, rs -> rs.getLong(1)).asBatch()
                    .startBatch()
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .endBatch();
        }

        @Test(expected = IllegalStateException.class)
        public void batchAlreadyEnded() throws Exception {
            final NewStudent student = new NewStudent("Ada10", "Dada10", new BigDecimal("3.1"));

            //noinspection deprecation
            final BatchedInsertReturnKeysQuery<Long>.Batch batch = JDBJ.insertQueryGetKeys(Student.insert, rs -> rs.getLong(1)).asBatch()
                    .startBatch()
                    .bindString(":first_name", student.firstName)
                    .bindString(":last_name", student.lastName)
                    .bindBigDecimal(":gpa", student.gpa);

            batch.endBatch();
            batch.bindString(":first_name", student.firstName);
        }
    }

    public static class Transaction {

        @ClassRule
        public static TestRule create_table = Student.createTableRule;

        @After
        public void tearDown() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
                ps.execute();
            }
        }

        final Student student = new Student(10L, "Ada", "Dada", new BigDecimal("3.1"));

        final UpdateQuery updateQuery = JDBJ.insertQuery(Student.insert_id)
                .bindLong(":id", student.id)
                .bindString(":first_name", student.firstName)
                .bindString(":last_name", student.lastName)
                .bindBigDecimal(":gpa", student.gpa);

        @Test
        public void committed() throws Exception {
            JDBJ.transaction(db, connection -> assertEquals(1, updateQuery.execute(connection)));

            final List<Student> actual;
            try (Connection connection = db.getConnection()) {
                actual = Student.selectAll.execute(connection);
            }

            assertEquals(Collections.singletonList(student), actual);
        }

        @Test
        public void returning() throws Exception {
            final List<Student> actual = JDBJ.returningTransaction(db, connection -> {
                assertEquals(1, updateQuery.execute(connection));
                return Student.selectAll.execute(connection);
            });

            assertEquals(Collections.singletonList(student), actual);
        }

        @Test
        public void returningWithIsolation() throws Exception {
            final List<Student> actual = JDBJ.returningTransaction(db, Connection.TRANSACTION_READ_COMMITTED, connection -> {
                assertEquals(1, updateQuery.execute(connection));
                return Student.selectAll.execute(connection);
            });

            assertEquals(Collections.singletonList(student), actual);
        }

        @Test
        public void rollback() throws Exception {
            try {
                JDBJ.transaction(db, connection -> {
                    assertEquals(1, updateQuery.execute(connection));
                    throw new SQLException("did I do that?");
                });
                fail("should have throw steve urkel exception");
            } catch (SQLException e) {
                assertEquals("did I do that?", e.getMessage());
            }

            final List<Student> actual;
            try (Connection connection = db.getConnection()) {
                actual = Student.selectAll.execute(connection);
            }
            assertTrue(actual.isEmpty());
        }

        @Test
        public void autoCommitTurnedOff() throws Exception {
            try (Connection connection = db.getConnection()) {
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

                assertTrue(connection.getAutoCommit());
                JDBJ.transaction(fakeDataSource, c -> {
                    assertFalse(c.getAutoCommit());
                    assertEquals(1, updateQuery.execute(c));
                });
                assertTrue(connection.getAutoCommit());
            }
        }

        @Test
        public void transactionIsolationReset() throws Exception {
            try (Connection connection = db.getConnection()) {
                final int originalIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;

                connection.setTransactionIsolation(originalIsolation);
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

                JDBJ.transaction(fakeDataSource, Connection.TRANSACTION_READ_COMMITTED, c -> {
                    assertEquals(Connection.TRANSACTION_READ_COMMITTED, c.getTransactionIsolation());
                    assertEquals(1, updateQuery.execute(c));
                });
                assertEquals(originalIsolation, connection.getTransactionIsolation());
            }
        }

        @Test
        public void transactionIsolationNotResetIfNotProvided() throws Exception {
            try (Connection connection = db.getConnection()) {
                final int originalIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;

                connection.setTransactionIsolation(originalIsolation);
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

                JDBJ.transaction(fakeDataSource, c -> {
                    assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, c.getTransactionIsolation());
                    assertEquals(1, updateQuery.execute(c));
                });
                assertEquals(originalIsolation, connection.getTransactionIsolation());
            }
        }


        @Test(expected = IllegalStateException.class)
        public void exceptIfAutocommitAlreadyOff() throws Exception {
            try (Connection connection = db.getConnection()) {
                DataSource fakeDataSource = new FakeDataSource<>(() -> connection);
                connection.setAutoCommit(false);
                JDBJ.transaction(fakeDataSource, c -> assertEquals(1, updateQuery.execute(c)));
            }
        }

        @Test
        public void exceptDuringAutocommitResetIgnored() throws Exception {
            try (Connection connection = db.getConnection()) {
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                    @Override
                    public void setAutoCommit(boolean autoCommit) throws SQLException {
                        super.setAutoCommit(autoCommit);
                        if (autoCommit) {
                            throw new SQLException();
                        }
                    }

                    @Override
                    public void close() throws SQLException {
                        connection.close();
                    }
                });
                JDBJ.transaction(fakeDataSource, c -> assertEquals(1, updateQuery.execute(c)));
                assertTrue(connection.isClosed());
            }
        }

        @Test
        public void exceptDuringIsolationResetIgnored() throws Exception {
            try (Connection connection = db.getConnection()) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                    @Override
                    public void setTransactionIsolation(int level) throws SQLException {
                        final int beforeSet = connection.getTransactionIsolation();
                        super.setTransactionIsolation(level);
                        if (beforeSet == Connection.TRANSACTION_READ_COMMITTED) {
                            throw new SQLException("should be ignored");
                        }
                    }

                    @Override
                    public void close() throws SQLException {
                        connection.close();
                    }
                });
                JDBJ.transaction(fakeDataSource, Connection.TRANSACTION_READ_COMMITTED, c -> assertEquals(1, updateQuery.execute(c)));
                assertTrue(connection.isClosed());
            }
        }

        @Test
        public void exceptDuringCloseIgnored() throws Exception {
            try (Connection connection = db.getConnection()) {
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                    @Override
                    public void close() throws SQLException {
                        connection.close();
                        throw new SQLException("should be ignored");
                    }
                });
                JDBJ.transaction(fakeDataSource, c -> assertEquals(1, updateQuery.execute(c)));
                assertTrue(connection.isClosed());
            }
        }

        @Test
        public void exceptDuringRollbackIgnored() throws Exception {
            try (Connection connection = db.getConnection()) {
                DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {

                    @Override
                    public void rollback() throws SQLException {
                        super.rollback();
                        throw new SQLException("should be ignored");
                    }

                    @Override
                    public void close() throws SQLException {
                        connection.close();
                    }
                });
                try {
                    JDBJ.transaction(fakeDataSource, c -> {
                        assertEquals(1, updateQuery.execute(c));
                        throw new SQLException("did I do that?");
                    });
                    fail("should have throw steve urkel exception");
                } catch (SQLException e) {
                    assertEquals("did I do that?", e.getMessage());
                }
                assertTrue(connection.isClosed());
            }
        }
    }

    private static class NewStudent {

        static final String insert = "student_insert.sql";

        final String firstName;
        final String lastName;
        final BigDecimal gpa;

        public NewStudent(String firstName, String lastName, BigDecimal gpa) {
            if (firstName == null) {
                throw new IllegalArgumentException();
            }
            if (lastName == null) {
                throw new IllegalArgumentException();
            }
            if (gpa == null) {
                throw new IllegalArgumentException();
            }
            this.firstName = firstName;
            this.lastName = lastName;
            this.gpa = gpa;
        }

        public Student withId(long id) {
            return new Student(id, firstName, lastName, gpa);
        }
    }

    private static class Student extends NewStudent {

        public static final TestRule createTableRule = (base, description) -> new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String createStudents = "CREATE TABLE student(id BIGINT PRIMARY KEY auto_increment, first_name varchar, last_name varchar, gpa varchar)";
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

        static final String insert_id = "student_insert_id.sql";

        static final ExecuteQuery<List<Student>> selectAll = JDBJ.queryString("SELECT * FROM student ORDER BY id ASC")
                .map(Student::from)
                .toList();

        static Student from(ResultSet rs) throws SQLException {
            return new Student(rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getBigDecimal("gpa"));
        }

        final long id;

        public Student(long id, String firstName, String lastName, BigDecimal gpa) {
            super(firstName, lastName, gpa);
            this.id = id;
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
