package io.codemonastery.jdbj.test.query;

import io.codemonastery.jdbj.ExecuteUpdate;
import io.codemonastery.jdbj.JDBJ;
import io.codemonastery.jdbj.lambda.ConnectionCallable;
import io.codemonastery.jdbj.lambda.ConnectionSupplier;
import io.codemonastery.jdbj.student.Student;
import io.codemonastery.jdbj.student.NewStudent;
import io.codemonastery.jdbj.student.StudentTest;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class ReturningTransactionTest extends StudentTest {

    protected final NewStudent student = new NewStudent("Ada", "Dada", new BigDecimal("3.1"));

    protected final ExecuteUpdate executeUpdate = JDBJ.resource(Student.INSERT).update()
            .bindString(":first_name", student.getFirstName())
            .bindString(":last_name", student.getLastName())
            .bindBigDecimal(":gpa", student.getGpa());

    protected final int highIsolation;
    protected final int lowIsolation;

    public ReturningTransactionTest() {
        this( Connection.TRANSACTION_SERIALIZABLE, Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    public ReturningTransactionTest(int highIsolation, int lowIsolation) {
        this.highIsolation = highIsolation;
        this.lowIsolation = lowIsolation;
    }

    @Test
    public void returning() throws Exception {
        ConnectionCallable<List<Student>> callable = connection -> {
            assertEquals(1, executeUpdate.execute(connection));
            return Student.SELECT_ALL.execute(connection);
        };
        final List<Student> actual = JDBJ.transaction(callable).execute(db());

        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test
    public void returningWithIsolation() throws Exception {
        ConnectionCallable<List<Student>> callable = connection -> {
            assertEquals(1, executeUpdate.execute(connection));
            return Student.SELECT_ALL.execute(connection);
        };
        final List<Student> actual = JDBJ.transaction(callable).isolation(highIsolation).execute(db());

        assertEquals(1, actual.size());
        assertEquals(student.getFirstName(), actual.get(0).getFirstName());
    }

    @Test(expected = NullPointerException.class)
    public void executeNullDataSource() throws Exception {
        JDBJ.transaction(connection-> "a").execute((DataSource) null);
    }

    @Test(expected = NullPointerException.class)
    public void executeNullConnectionSupplier() throws Exception {
        JDBJ.transaction(connection-> "a").execute((ConnectionSupplier) null);
    }

    @Test(expected = NullPointerException.class)
    public void executeNullConnection() throws Exception {
        JDBJ.transaction(connection-> "a").execute((Connection) null);
    }
    
}
