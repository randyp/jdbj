[![Build Status](https://travis-ci.org/randyp/jdbj.svg?branch=master)](https://travis-ci.org/randyp/jdbj)
[![Coverage Status](https://coveralls.io/repos/randyp/jdbj/badge.svg?branch=master&service=github)](https://coveralls.io/github/randyp/jdbj?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/)

JDBJ is a small jdbc fluent interface for capturing query intent before query execution.

#### See Also
 * [Wiki](/wiki) For api documentation, guiding principles, credits.

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
final List<NewStudent> newStudents = Arrays.asList(
        new NewStudent("Ada", "Lovelace", new BigDecimal("4.00")),
        new NewStudent("Haskell", "Curry", new BigDecimal("4.00"))
);
final ExecuteInsert<Long> insert = JDBJ.resource(NewStudent.insert)
        .insert(rs->rs.getLong(1));

//db is a javax.sql.DataSource or com.github.randyp.jdbj.lambda.ConnectionSupplier
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
A full set of examples is being developed in [jdbj-examples](https://github.com/randyp/jdbj-examples), but here are some quick examples maintained in the test directory:
* [Example Main](src/test/java/com/github/randyp/jdbj/example/InformationSchemaMain.java)
* [Example DAO (connection as construction parameter)](src/test/java/com/github/randyp/jdbj/example/StudentDAO.java)
* [Example DAO (binding/result set extension)](src/test/java/com/github/randyp/jdbj/example/extension/MessageDAO.java)


#### Release
``` sh
./release.sh
```
