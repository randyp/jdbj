package com.github.randyp.jdbj.example;

import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.MapQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class StudentDAO {

    private static final MapQuery<Student> studentsByID = JDBJ
            .queryString("SELECT id, first_name, last_name, gpa FROM students WHERE id in :ids")
            .map(Student::from);

    private final Connection connection;


    public StudentDAO(Connection connection) {
        final MapQuery<Student> studentsByIds = JDBJ.queryString("student_by_ids.sql")
                .map(Student::from);
        try(Connection connection = db.getConnection) {
            final List<Student> students = studentsByIds
                    .toList()
                    .bindLongs(":ids", 1L, 2L, 3L, 11L, 12L, 14L)
                    .execute(connection);
        }
        this.connection = connection;
    }

    public Student byId(long id) throws SQLException {
        return studentsByID.first().bindLongs(":ids", id).execute(connection);
    }

    public List<Student> byIds(long[] ids) throws SQLException {
        return studentsByID.toList().bindLongs(":ids", ids).execute(connection);
    }

    public Stream<Student> streamByIds(long[] ids) throws SQLException {
        return studentsByID.stream().bindLongs(":ids", ids).execute(connection); //close me when done!!!
    }
}
