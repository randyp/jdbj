package io.github.randyp.jdbj.example;

import io.github.randyp.jdbj.*;
import io.github.randyp.jdbj.db.h2_1_4.H2DB;
import io.github.randyp.jdbj.student.NewStudent;
import io.github.randyp.jdbj.student.Student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ReadmeMain {

    public static void main(String[] args) throws Exception {
        try (H2DB db = new H2DB("PrototypeMain")) {
            //INSERT some students
            final List<NewStudent> newStudents = Arrays.asList(
                    new NewStudent("Ada", "Lovelace", new BigDecimal("4.00")),
                    new NewStudent("Haskell", "Curry", new BigDecimal("4.00"))
            );
            final ExecuteInsert<Long> insert = JDBJ.resource("student_insert.sql")
                    .insert(rs->rs.getLong(1));

            //db is a javax.sql.DataSource
            List<Long> generatedKeys = JDBJ.returningTransaction(connection -> {
                final List<Long> keys = new ArrayList<>();
                for (NewStudent newStudent : newStudents) {
                    keys.addAll(insert.bindValues(newStudent::bindings).execute(connection));
                }
                return keys;
            }).execute(db);

            //setup query object
            final MapQuery<Student> studentsByIds = JDBJ.resource("student_by_ids_limit.sql")
                    .query()
                    .bindLong(":limit", 10L)
                    .bindLongs(":ids", generatedKeys)
                    .map(Student::from);

            //get as list
            final ExecuteQuery<List<Student>> listQuery = studentsByIds
                    .toList();
            System.out.println(listQuery.execute(db));

            //get as stream
            final StreamQuery<Student> streamQuery = studentsByIds
                    .toStream();
            try (Stream<Student> stream = streamQuery.execute(db)) {
                stream.forEach(System.out::println);
            }
        }
    }
}
