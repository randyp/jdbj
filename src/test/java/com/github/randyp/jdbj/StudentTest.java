package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import org.junit.After;
import org.junit.ClassRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class StudentTest {

    @ClassRule
    public static final H2Rule db = dbRule();

    @After
    public void tearDown() throws Exception {
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM student")) {
            ps.execute();
        }
    }

    public static H2Rule dbRule(){
        return new H2Rule(){
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
    }
}
