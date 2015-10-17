package io.codemonastery.jdbj.student;

import io.codemonastery.jdbj.db.h2_1_4.H2Rule;
import io.codemonastery.jdbj.test.binding.value.DBSupplier;
import org.junit.After;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class StudentTest implements DBSupplier {

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

    public static H2Rule dbRule(){
        return new H2Rule(){
            @Override
            protected void before() throws Throwable {
                super.before();
                cleanup();
                final String createStudents = "CREATE TABLE student(id BIGINT PRIMARY KEY auto_increment, first_name varchar, last_name varchar, gpa varchar)";
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
                    final String dropStudents = "DROP TABLE student";
                    try (Connection connection = db.getConnection();
                         PreparedStatement preparedStatement = connection.prepareStatement(dropStudents)) {
                        preparedStatement.execute();
                    }
                } catch (SQLException e) {
                    //ignore
                }
            }
        };
    }
}
