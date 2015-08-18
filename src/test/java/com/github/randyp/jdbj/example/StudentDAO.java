package com.github.randyp.jdbj.example;

import com.github.randyp.jdbj.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class StudentDAO {

    private final Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    private static final ExecuteQuery<List<Student>> all = JDBJ.resource("student_all_ordered_by_id.sql")
            .query()
            .map(Student::from)
            .toList()
            .bindDefaultLong(":limit", 10L);

    public List<Student> all(Optional<Long> limit) throws SQLException {
        ExecuteQuery<List<Student>> all = StudentDAO.all;
        if(limit.isPresent()){
            all = all.bindLong(":limit", limit.get());
        }
        return all.execute(connection);
    }

    public List<Student> all() throws SQLException {
        return all.execute(connection);
    }

    private static final MapQuery<Student> by_ids = JDBJ.resource("student_by_ids.sql")
            .query()
            .map(Student::from);

    public Optional<Student> byId(long id) throws SQLException {
        return by_ids.first().bindLongs(":ids", id).execute(connection);
    }

    public List<Student> byIds(long[] ids) throws SQLException {
        return by_ids.toList().bindLongs(":ids", ids).execute(connection);
    }

    public Stream<Student> streamByIds(long[] ids) throws SQLException {
        return by_ids.stream().bindLongs(":ids", ids).execute(connection); //close me when done!!!
    }

    private static final ExecuteInsert<Long> insert = JDBJ.resource("student_insert.sql")
            .insert(rs->rs.getLong(1));

    public Student insert(NewStudent newStudent) throws SQLException{
        final List<Long> execute = insert.bindString(":first_name", newStudent.getFirstName())
                .bindString(":last_name", newStudent.getLastName())
                .bindBigDecimal(":gpa", newStudent.getGpa())
                .execute(connection);
        final Long id = execute.stream().findFirst().get();
        return byId(id).get();
    }
}
