package com.github.randyp.jdbj.example;

import com.github.randyp.jdbj.JDBJ;

import com.github.randyp.jdbj.MapQuery;
import com.github.randyp.jdbj.Student;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class StudentDAO {

    private final Connection connection;


    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    private static final MapQuery<Student> by_ids = JDBJ.string("SELECT id, first_name, last_name, gpa FROM students WHERE id in :ids").query()
            .map(Student::from);

    public Student byId(long id) throws SQLException {
        return by_ids.first().bindLongs(":ids", id).execute(connection);
    }

    public List<Student> byIds(long[] ids) throws SQLException {
        return by_ids.toList().bindLongs(":ids", ids).execute(connection);
    }

    public Stream<Student> streamByIds(long[] ids) throws SQLException {
        return by_ids.stream().bindLongs(":ids", ids).execute(connection); //close me when done!!!
    }
}
