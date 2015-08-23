[![Build Status](https://travis-ci.org/randyp/jdbj.svg?branch=master)](https://travis-ci.org/randyp/jdbj)
[![Coverage Status](https://coveralls.io/repos/randyp/jdbj/badge.svg?branch=master&service=github)](https://coveralls.io/github/randyp/jdbj?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/)

jdbj is a small jdbc fluent interface for capturing query intent before query execution. Sample code:
``` java
//insert some students
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
```

#### Features
* Named Parameters (No Positional Parameters, ever)
* Collections bindable (not during batch execution)
* Null-Safe fetching of (Boolean|Byte|Double|Float|Integer|Long|Short)
* Java8 lambda interface for bindings, transactions
* Fetch-forward read-only cursors, always
* Script Execution, with parameters
* Comprehensive test suites for latest postgres,h2,derby,SQLite,MySql

#### Examples
A full set of examples is being developed in [jdbj-examples](https://github.com/randyp/jdbj-examples), but here are some quick examples.

* [Example Main](src/test/java/com/github/randyp/jdbj/example/InformationSchemaMain.java)
* [Example DAO (connection as construction parameter)](src/test/java/com/github/randyp/jdbj/example/StudentDAO.java)
* [Example DAO (binding/result set extension)](src/test/java/com/github/randyp/jdbj/example/extension/MessageDAO.java)

#### Usage
Just click on the Maven Central badge above. Can download from there or grab the dependency tag for the desired version. Will look something like this...

``` xml
<dependency>
    <groupId>com.github.randyp</groupId>
    <artifactId>jdbj</artifactId>
    <version>${jdbj.version}</version>
</dependency>
```

#### Versioning
jdbj is not following any semantic versioning scheme yet. After we have some users we'll release version 1 and follow apache versioning. Until then, expect us to break interfaces.

#### Why?
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "build, build, execute". We get to reuse build steps across most requests and only do the minimum amount of query building for each request.

In addition, many other convenience libraries try to hide the binding code behind annotations and reflection. JDBJ takes a different approach, and exposes jdbc-similar bindings without giving you direct access to PreparedStatement. 

#### Guiding Principles
* No *connection handles* - just use the Connection as an argument
* Bubble up the SQLException, or wrap if not possible (eg. java.util.Stream#tryAdvance)
* Immutable builders to capture the query intent (except Batched Builders, which are mutable)
* Use PreparedColumn to hide PreparedStatement
* Named Parameters only, no Positional Parameters
* Fetch-forward, read-only cursors for read only queries
* No type guessing - you specified the type when you called `something.bindLong(":id", 10)`
* No external dependencies (where did my guava go?)

#### Credits
Thank you to:
* Carmine Mangione for showing me that it is OK to use jdbc
* [jdbi team](http://jdbi.org/) for authoring jdbi, as some of jdbj is based on jdbi.

#### Release
``` sh
./release.sh
```
