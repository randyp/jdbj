package com.github.randyp.jdbj.db.h2_1_4;

import com.github.randyp.jdbj.test.query.ExecuteQueryRunnableTest;
import com.github.randyp.jdbj.test.query.ExecuteQueryTest;
import com.github.randyp.jdbj.test.query.ExecuteScriptTest;
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
    public static final H2Rule db = new H2Rule(){
        @Override
        protected void before() throws Throwable {
            super.before();
            final String createStudents = "CREATE TABLE student(id BIGINT PRIMARY KEY auto_increment, first_name varchar, last_name varchar, gpa varchar)";
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

}
