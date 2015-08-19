package com.github.randyp.jdbj.db.postgres_9_4;

import com.github.randyp.jdbj.test.query.ExecuteQueryRunnableTest;
import com.github.randyp.jdbj.test.query.ExecuteQueryTest;
import com.github.randyp.jdbj.test.query.ExecuteScriptTest;
import com.github.randyp.jdbj.test.query.ExecuteStatementTest;
import org.junit.ClassRule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RunWith(Enclosed.class)
public class QueriesTest {

    @ClassRule
    public static final PGRule db = new PGRule(){
        @Override
        protected void before() throws Throwable {
            super.before();
            cleanup();
            final String createStudents = "CREATE TABLE student(id SERIAL PRIMARY KEY, first_name varchar, last_name varchar, gpa varchar)";
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(createStudents)) {
                preparedStatement.execute();
            }
        }

        @Override
        protected void after() {
            cleanup();
            super.after();
        }

        private void cleanup() {
            try {
                final String dropStudents = "DROP TABLE IF EXISTS student";
                try (Connection connection = db.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(dropStudents)) {
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                //ignore
            }
        }
    };

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
}
