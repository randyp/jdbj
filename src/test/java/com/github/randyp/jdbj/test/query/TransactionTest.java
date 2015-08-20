package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.student.NewStudent;
import com.github.randyp.jdbj.student.Student;
import com.github.randyp.jdbj.student.StudentTest;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public abstract class TransactionTest extends StudentTest {

    final NewStudent student = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));

    final ExecuteUpdate executeUpdate = JDBJ.resource(Student.insert).update()
            .bindString(":first_name", student.getFirstName())
            .bindString(":last_name", student.getLastName())
            .bindBigDecimal(":gpa", student.getGpa());

    @Test
    public void committed() throws Exception {
        JDBJ.transaction(db(), connection -> assertEquals(1, executeUpdate.execute(connection)));

        final List<Student> actual = Student.selectAll.execute(db());
        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void returning() throws Exception {
        final List<Student> actual = JDBJ.returningTransaction(db(), connection -> {
            assertEquals(1, executeUpdate.execute(connection));
            return Student.selectAll.execute(connection);
        });

        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void returningWithIsolation() throws Exception {
        final List<Student> actual = JDBJ.returningTransaction(db(), Connection.TRANSACTION_READ_COMMITTED, connection -> {
            assertEquals(1, executeUpdate.execute(connection));
            return Student.selectAll.execute(connection);
        });

        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void rollback() throws Exception {
        try {
            JDBJ.transaction(db(), connection -> {
                assertEquals(1, executeUpdate.execute(connection));
                throw new SQLException("did I do that?");
            });
            fail("should have throw steve urkel exception");
        } catch (SQLException e) {
            assertEquals("did I do that?", e.getMessage());
        }

        final List<Student> actual = Student.selectAll.execute(db());
        assertTrue(actual.isEmpty());
    }

    @Test
    public void autoCommitTurnedOff() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            assertTrue(connection.getAutoCommit());
            JDBJ.transaction(fakeDataSource, c -> {
                assertFalse(c.getAutoCommit());
                assertEquals(1, executeUpdate.execute(c));
            });
            assertTrue(connection.getAutoCommit());
        }
    }

    @Test
    public void transactionIsolationReset() throws Exception {
        try (Connection connection = db().getConnection()) {
            final int originalIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;

            connection.setTransactionIsolation(originalIsolation);
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            JDBJ.transaction(fakeDataSource, Connection.TRANSACTION_READ_COMMITTED, c -> {
                assertEquals(Connection.TRANSACTION_READ_COMMITTED, c.getTransactionIsolation());
                assertEquals(1, executeUpdate.execute(c));
            });
            assertEquals(originalIsolation, connection.getTransactionIsolation());
        }
    }


    @Test
    public void transactionIsolationNotResetIfNotProvided() throws Exception {
        try (Connection connection = db().getConnection()) {
            final int originalIsolation = Connection.TRANSACTION_READ_UNCOMMITTED;

            connection.setTransactionIsolation(originalIsolation);
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            JDBJ.transaction(fakeDataSource, c -> {
                assertEquals(Connection.TRANSACTION_READ_UNCOMMITTED, c.getTransactionIsolation());
                assertEquals(1, executeUpdate.execute(c));
            });
            assertEquals(originalIsolation, connection.getTransactionIsolation());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void exceptIfAutocommitAlreadyOff() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> connection);
            connection.setAutoCommit(false);
            JDBJ.transaction(fakeDataSource, c -> assertEquals(1, executeUpdate.execute(c)));
        }
    }

    @Test
    public void exceptDuringAutocommitResetIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
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
            JDBJ.transaction(fakeDataSource, c -> assertEquals(1, executeUpdate.execute(c)));
            assertTrue(connection.isClosed());
        }
    }

    @Test
    public void exceptDuringIsolationResetIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
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
            JDBJ.transaction(fakeDataSource, Connection.TRANSACTION_READ_COMMITTED, c -> assertEquals(1, executeUpdate.execute(c)));
            assertTrue(connection.isClosed());
        }
    }

    @Test
    public void exceptDuringCloseIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                @Override
                public void close() throws SQLException {
                    connection.close();
                    throw new SQLException("should be ignored");
                }
            });
            JDBJ.transaction(fakeDataSource, c -> assertEquals(1, executeUpdate.execute(c)));
            assertTrue(connection.isClosed());
        }
    }

    @Test
    public void exceptDuringRollbackIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
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
                    assertEquals(1, executeUpdate.execute(c));
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
