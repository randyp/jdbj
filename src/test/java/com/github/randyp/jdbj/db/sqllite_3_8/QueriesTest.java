package com.github.randyp.jdbj.db.sqllite_3_8;

import com.github.randyp.jdbj.test.query.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RunWith(Enclosed.class)
public class QueriesTest {

    @ClassRule
    public static final SqlLiteRule db = new SqlLiteRule(){
        @Override
        protected void before() throws Throwable {
            super.before();
            final String createStudents = "CREATE TABLE student(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name varchar(500), last_name varchar(500), gpa varchar(10))";
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(createStudents)) {
                preparedStatement.execute();
            }
        }

        @Override
        protected void after() {
            try {
                final String dropStudents = "DROP TABLE student";
                try (Connection connection = db.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(dropStudents)) {
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                //ignore
            }
            super.after();
        }
    };

    public static class BatchedExecuteInsert extends BatchedExecuteInsertTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = AssertionError.class) //because sqllite doesn't return multiple keys from execute batch
        @Override
        public void insertBatches() throws Exception {
            super.insertBatches();
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
}
