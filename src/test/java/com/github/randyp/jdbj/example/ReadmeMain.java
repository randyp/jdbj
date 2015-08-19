package com.github.randyp.jdbj.example;

import com.github.randyp.jdbj.*;
import com.github.randyp.jdbj.db.h2_1_4.H2DB;
import com.github.randyp.jdbj.student.Student;

import java.util.List;
import java.util.stream.Stream;

public class ReadmeMain {

    public static void main(String[] args) throws Exception {
        try (H2DB db = new H2DB("PrototypeMain")) {
            final MapQuery<Student> studentsByIds = JDBJ.resource("student_by_ids.sql")
                    .query()
                    .bindDefaultLong(":limit", 10L)
                    .map(Student::from);

            //do something else for a while
            final ExecuteQuery<List<Student>> listQuery = studentsByIds
                    .toList()
                    .bindLongs(":ids", 1L, 2L, 3L, 11L, 12L, 14L);
            System.out.println(listQuery.execute(db));

            //do something else for a while
            final StreamQuery<Student> streamQuery = studentsByIds
                    .toStream()
                    .bindLongs(":ids", 10L, 11L, 12L);
            try (Stream<Student> stream = streamQuery.execute(db)) {
                stream.forEach(System.out::println);
            }
        }
    }
}
