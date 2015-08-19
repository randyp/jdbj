package com.github.randyp.jdbj.postgres9_4;

import org.junit.After;
import org.junit.ClassRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class StudentTest {

    @ClassRule
    public static DBRule db = new DBRule(){

        @Override
        protected void before() throws Throwable {
            super.before();

            clean();

            final String createStudents = "CREATE TABLE student(id SERIAL PRIMARY KEY, first_name varchar, last_name varchar, gpa varchar)";
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(createStudents)) {
                preparedStatement.execute();
            }
        }

        @Override
        protected void after() {
            clean();
            super.after();
        }

        private void clean() {
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

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

}
