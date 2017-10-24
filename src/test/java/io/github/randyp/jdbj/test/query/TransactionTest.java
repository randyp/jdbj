package io.github.randyp.jdbj.test.query;

import io.github.randyp.jdbj.ExecuteUpdate;
import io.github.randyp.jdbj.FakeConnection;
import io.github.randyp.jdbj.FakeDataSource;
import io.github.randyp.jdbj.student.NewStudent;
import io.github.randyp.jdbj.student.Student;
import io.github.randyp.jdbj.student.StudentTest;
import io.github.randyp.jdbj.JDBJ;
import io.github.randyp.jdbj.lambda.ConnectionRunnable;
import io.github.randyp.jdbj.lambda.ConnectionSupplier;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public abstract class TransactionTest extends StudentTest {

    protected final NewStudent student = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));

    protected final ExecuteUpdate executeUpdate = JDBJ.resource(Student.INSERT).update()
            .bindString(":first_name", student.getFirstName())
            .bindString(":last_name", student.getLastName())
            .bindBigDecimal(":gpa", student.getGpa());

    protected final int highIsolation;
    protected final int lowIsolation;

    public TransactionTest() {
        this( Connection.TRANSACTION_SERIALIZABLE, Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    public TransactionTest(int highIsolation, int lowIsolation) {
        this.highIsolation = highIsolation;
        this.lowIsolation = lowIsolation;
    }

    @Test
    public void committed() throws Exception {
        ConnectionRunnable runnable = connection -> assertEquals(1, executeUpdate.execute(connection));
        JDBJ.transaction(runnable).execute(db());

        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void committedUsingConnection() throws Exception {
        ConnectionRunnable runnable = connection -> assertEquals(1, executeUpdate.execute(connection));
        try(Connection connection = db().getConnection()){
            connection.setTransactionIsolation(lowIsolation);
            JDBJ.transaction(runnable).isolation(highIsolation).execute(connection);
            
            assertFalse(connection.isClosed());
            assertTrue(connection.getAutoCommit());
            assertEquals(lowIsolation, connection.getTransactionIsolation());
        }

        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void rollback() throws Exception {
        try {
            ConnectionRunnable runnable = connection -> {
                assertEquals(1, executeUpdate.execute(connection));
                throw new SQLException("did I do that?");
            };
            JDBJ.transaction(runnable).execute(db());
            fail("should have throw steve urkel exception");
        } catch (SQLException e) {
            assertEquals("did I do that?", e.getMessage());
        }

        final List<Student> actual = Student.SELECT_ALL.execute(db());
        assertTrue(actual.isEmpty());
    }

    @Test
    public void autoCommitTurnedOff() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            assertTrue(connection.getAutoCommit());
            ConnectionRunnable runnable = c -> {
                assertFalse(c.getAutoCommit());
                assertEquals(1, executeUpdate.execute(c));
            };
            JDBJ.transaction(runnable).execute(fakeDataSource);
            assertTrue(connection.getAutoCommit());
        }
    }

    @Test
    public void transactionIsolationReset() throws Exception {
        try (Connection connection = db().getConnection()) {
            final int originalIsolation = lowIsolation;

            //noinspection MagicConstant
            connection.setTransactionIsolation(originalIsolation);
            assertEquals(originalIsolation, connection.getTransactionIsolation());
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            ConnectionRunnable runnable = c -> {
                assertEquals(highIsolation, c.getTransactionIsolation());
                assertEquals(1, executeUpdate.execute(c));
            };
            JDBJ.transaction(runnable).isolation(highIsolation).execute(fakeDataSource);
            assertEquals(originalIsolation, connection.getTransactionIsolation());
        }
    }

    @Test
    public void transactionIsolationNotResetIfNotProvided() throws Exception {
        try (Connection connection = db().getConnection()) {
            final int originalIsolation = lowIsolation;

            //noinspection MagicConstant
            connection.setTransactionIsolation(originalIsolation);
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection));

            ConnectionRunnable runnable = c -> {
                assertEquals(originalIsolation, c.getTransactionIsolation());
                assertEquals(1, executeUpdate.execute(c));
            };
            JDBJ.transaction(runnable).execute(fakeDataSource);
            assertEquals(originalIsolation, connection.getTransactionIsolation());
        }
    }

    @Test(expected = SQLException.class)
    public void exceptIfAutocommitAlreadyOff() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> connection);
            connection.setAutoCommit(false);
            ConnectionRunnable runnable = c -> assertEquals(1, executeUpdate.execute(c));
            JDBJ.transaction(runnable).execute(fakeDataSource);
        }
    }

    @Test(expected = SQLException.class)
    public void exceptDuringAutocommitResetNotIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                @Override
                public void setAutoCommit(boolean autoCommit) throws SQLException {
                    super.setAutoCommit(autoCommit);
                    if (autoCommit) {
                        throw new SQLException("do not ignore me");
                    }
                }

                @Override
                public void close() throws SQLException {
                    connection.close();
                }
            });
            ConnectionRunnable runnable = c -> assertEquals(1, executeUpdate.execute(c));
            try{
                JDBJ.transaction(runnable).execute(fakeDataSource);
            }catch (Exception e){
                assertTrue(connection.isClosed());
                throw e;
            }
        }
    }

    @Test(expected = SQLException.class)
    public void exceptDuringIsolationResetNotIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
            connection.setTransactionIsolation(lowIsolation);
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                @Override
                public void setTransactionIsolation(int level) throws SQLException {
                    final int beforeSet = connection.getTransactionIsolation();
                    super.setTransactionIsolation(level);
                    if (beforeSet == highIsolation) {
                        throw new SQLException("do not ignore me");
                    }
                }

                @Override
                public void close() throws SQLException {
                    connection.close();
                }
            });
            ConnectionRunnable runnable = c -> assertEquals(1, executeUpdate.execute(c));
            try{
                JDBJ.transaction(runnable).isolation(highIsolation).execute(fakeDataSource);
            }catch (Exception e){
                assertTrue(connection.isClosed());
                throw e;
            }
        }
    }

    @Test(expected = SQLException.class)
    public void exceptDuringCloseIgnored() throws Exception {
        ConnectionRunnable runnable = c -> assertEquals(1, executeUpdate.execute(c));
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {
                @Override
                public void close() throws SQLException {
                    connection.close();
                    throw new SQLException("do not ignore me");
                }
            });
            
            try{
                JDBJ.transaction(runnable).execute(fakeDataSource);
            }catch (Exception e){
                assertTrue(connection.isClosed());
                throw e;
            }
        }
    }

    @Test
    public void exceptDuringRollbackIgnored() throws Exception {
        try (Connection connection = db().getConnection()) {
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {

                @Override
                public void rollback() throws SQLException {
                    super.rollback();
                    throw new SQLException("should not be ignored");
                }

                @Override
                public void close() throws SQLException {
                    connection.close();
                }
            });
            try {
                ConnectionRunnable runnable = c -> {
                    assertEquals(1, executeUpdate.execute(c));
                    throw new SQLException("did I do that?");
                };
                JDBJ.transaction(runnable).execute(fakeDataSource);
                fail("should have throw steve urkel exception");
            } catch (SQLException e) {
                assertEquals("did I do that?", e.getMessage());
                assertNotNull(e.getNextException());
                e = e.getNextException();
                assertEquals("should not be ignored", e.getMessage());
            }
            assertTrue(connection.isClosed());
        }
    }

    @Test
    public void exceptionsChainedProperlyDuringRollback() throws Exception {
        final int originalIsolation = this.lowIsolation;
        try (Connection connection = db().getConnection()) {
            connection.setTransactionIsolation(originalIsolation);
            DataSource fakeDataSource = new FakeDataSource<>(() -> new FakeConnection(connection) {

                @Override
                public void rollback() throws SQLException {
                    super.rollback();
                    throw new SQLException("should not be ignored - rollback");
                }

                @Override
                public void setAutoCommit(boolean autoCommit) throws SQLException {
                    super.setAutoCommit(autoCommit);
                    if(autoCommit){
                        throw new SQLException("should not be ignored - autocommit");
                    }
                }

                @Override
                public void setTransactionIsolation(int level) throws SQLException {
                    super.setTransactionIsolation(level);
                    if(level == originalIsolation){
                        throw new SQLException("should not be ignored - isolation");
                    }
                }

                @Override
                public void close() throws SQLException {
                    connection.close();
                    throw new SQLException("should not be ignored - close");
                }
            });
            try {
                ConnectionRunnable runnable = c -> {
                    assertEquals(1, executeUpdate.execute(c));
                    throw new SQLException("should not be ignored - runnable");
                };
                JDBJ.transaction(runnable).isolation(highIsolation).execute(fakeDataSource);
                fail("was supposed to except");
            } catch (SQLException e) {
                assertTrue(connection.isClosed());
                assertEquals("should not be ignored - runnable", e.getMessage());
                
                e = e.getNextException();
                assertNotNull(e);
                assertEquals("should not be ignored - rollback", e.getMessage());

                e = e.getNextException();
                assertNotNull(e);
                assertEquals("should not be ignored - autocommit", e.getMessage());

                e = e.getNextException();
                assertNotNull(e);
                assertEquals("should not be ignored - isolation", e.getMessage());

                e = e.getNextException();
                assertNotNull(e);
                assertEquals("should not be ignored - close", e.getMessage());
                
                assertNull(e.getNextException());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void executeNullDataSource() throws Exception {
        JDBJ.transaction(connection->{}).execute((DataSource) null);
    }

    @Test(expected = NullPointerException.class)
    public void executeNullConnectionSupplier() throws Exception {
        JDBJ.transaction(connection->{}).execute((ConnectionSupplier) null);
    }

    @Test(expected = NullPointerException.class)
    public void executeNullConnection() throws Exception {
        JDBJ.transaction(connection->{}).execute((Connection) null);
    }
}
