package io.github.randyp.jdbj.db.h2_1_4;

import io.github.randyp.jdbj.student.StudentTest;
import io.github.randyp.jdbj.test.query.*;
import org.junit.ClassRule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;

@RunWith(Enclosed.class)
public class QueriesTest {

    @ClassRule
    public static final H2Rule db = StudentTest.dbRule();

    public static class BatchedExecuteInsert extends BatchedExecuteInsertTest {

        @Override
        public DataSource db() {
            return db;
        }


    }

    public static class BatchedExecuteUpdate extends BatchedExecuteUpdateTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteInsert extends ExecuteInsertTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteQuery extends ExecuteQueryTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteQueryRunnable extends ExecuteQueryRunnableTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteScript extends ExecuteScriptTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteStatement extends ExecuteStatementTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ExecuteUpdate extends ExecuteUpdateTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class Transaction extends TransactionTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class ReturningTransaction extends ReturningTransactionTest {

        @Override
        public DataSource db() {
            return db;
        }
    }
}
