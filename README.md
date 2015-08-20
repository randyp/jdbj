[![Build Status](https://travis-ci.org/randyp/jdbj.svg?branch=master)](https://travis-ci.org/randyp/jdbj)
[![Coverage Status](https://coveralls.io/repos/randyp/jdbj/badge.svg?branch=master&service=github)](https://coveralls.io/github/randyp/jdbj?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/)

jdbj is an incredibly small jdbc fluent interface for capturing query intent before query execution. Sample code:
``` java
//db is a javax.sql.DataSource
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
```

#### Features
* Named Parameters (No Positional Parameters, ever)
* Collections bindable (not during batch execution)
* Null-Safe fetching of (Boolean|Byte|Double|Float|Integer|Long|Short)
* Java8 lambda interface for bindings, transactions
* Fetch-forward read-only cursors, always
* Utilities for best practices
* Script Execution, with parameters
* Integration tests with lastest postgres,h2,derby,sqllite,mysql

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
jdbj is not following any semantic versioning scheme yet, and will not until version 1, which we'll release once we have a user base. Expect us to break interfaces.

#### Why?
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "build, build, execute". We get to reuse build steps across most requests and only do the minimum amount of query building for each request.

In addition, many other convenience libraries try to hide the binding code behind annotations and reflection. JDBJ takes a different approach, and exposes jdbc-similar bindings without giving you direct access to PreparedStatement. 

#### Guiding Principles
* No *connection handles* - just use the Connection as an argument
* Bubble up the SQLException, or wrap if not possible (eg. java.util.Stream#tryAdvance)
* Immutable builders to capture the query intent, BatchedInsert Builders are mutable
* Use PreparedColumn to hide PreparedStatement
* Named Parameters only, no Positional Parameters
* Fetch-forward, read-only cursors
* No type guessing - you specified the type when you called `something.bindLong(":id", 10)`
* No external dependencies (where did my guava go?)

#### Credits
To the [jdbi team](http://jdbi.org/) for authoring jdbi, as much of jdbj is based on jdbi. Also:
``` java
System.out.println("jdb" + ((char) ('i' + 1)));
```

#### Release
``` sh
mvn release:clean release:prepare -B && mvn release:perform
```

#### 0.1.5 Todo
* [x] Derby Integration Tests
* [x] SQLLite Integration Tests
* [x] Mysql Integration Tests
* [ ] Hacky-yet-compatible batch insert & update queries

#### 0.1.6
* [ ] Somehow remove most antlr classes we don't need from shading process...
* [ ] Wrap Batch sql exceptions
* [ ] Java Doc
* [ ] Examples
