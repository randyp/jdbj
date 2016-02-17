[![Build Status](https://travis-ci.org/code-monastery/jdbj.svg?branch=master)](https://travis-ci.org/code-monastery/jdbj)
[![Coverage Status](https://img.shields.io/coveralls/code-monastery/jdbj.svg)](https://coveralls.io/r/code-monastery/jdbj)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.codemonastery/jdbj/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.codemonastery/jdbj)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/io.codemonastery/jdbj/badge.svg)](http://www.javadoc.io/doc/io.codemonastery/jdbj)

JDBJ is a small jdbc fluent interface for capturing query intent before query execution.

#### See Also
 * [Wiki](https://github.com/code-monastery/jdbj/wiki) For api documentation, guiding principles, credits.

#### Primary Features
* Named Parameters (No Positional Parameters, ever)
* Collections bindable (not during batch execution)
* Null-Safe fetching of (Boolean|Byte|Double|Float|Integer|Long|Short)
* Java8 lambda interface for bindings, transactions
* Fetch-forward read-only cursors, always
* Script Execution, with parameters
* Comprehensive test suites for latest postgres,h2,derby,SQLite,MySql 

#### Sample Code
Insert inside a transaction:
``` java
//INSERT some students
final List<NewStudent> newStudents = Arrays.asList(
        new NewStudent("Ada", "Lovelace", new BigDecimal("4.00")),
        new NewStudent("Haskell", "Curry", new BigDecimal("4.00"))
);

//NewStudent.INSERT is the resource which contains our sql statement
final ExecuteInsert<Long> insert = JDBJ.resource("student_insert.sql")
        .insert(rs->rs.getLong(1));

//db is a javax.sql.DataSource
List<Long> generatedKeys = JDBJ.transaction(connection -> {
    final List<Long> keys = new ArrayList<>();
    for (NewStudent newStudent : newStudents) {
        keys.addAll(insert.bindValues(newStudent::bindings).execute(connection));
    }
    return keys;
}).execute(db);
```

Select to list:
``` java
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
```

Select to stream:
``` java
//get as stream
final StreamQuery<Student> streamQuery = studentsByIds
        .toStream();
try (Stream<Student> stream = streamQuery.execute(db)) {
    stream.forEach(System.out::println);
}
```

#### Examples
A full set of examples is being developed in the [examples-branch](https://github.com/randyp/jdbj/tree/examples), but here are some quick examples maintained in the test directory:
* [Example Main](src/test/java/io/codemonastery/jdbj/example/InformationSchemaMain.java)
* [Example DAO (connection as construction parameter)](src/test/java/io/codemonastery/jdbj/example/StudentDAO.java)
* [Example DAO (binding/result set extension)](src/test/java/io/codemonastery/jdbj/example/extension/MessageDAO.java)

