[![Build Status](https://travis-ci.org/randyp/jdbj.svg?branch=master)](https://travis-ci.org/randyp/jdbj)
[![Coverage Status](https://coveralls.io/repos/randyp/jdbj/badge.svg?branch=master&service=github)](https://coveralls.io/github/randyp/jdbj?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.randyp/jdbj/)

jdbj is an incredibly small jdbc fluent interface for capturing query intent before query execution. Sample code:
``` java
final ExecuteQuery<List<Student>> studentsByIds = JDBJ.resource("student_by_ids.sql")
    .query()
    .map(Student::from)
    .toList()
    .bindDefaultLong(":limit", 10L);

//do something else for a while
try(Connection connection = db.getConnection) {
    final List<Student> students = studentsByIds
            .bindLongs(":ids", 1L, 2L, 3L, 11L, 12L, 14L)
            .execute(connection);
}
```

#### Features
* Named Parameters (No Positional Parameters, ever)
* List bindings (not during batch execution)
* Java8 lambda interface for bindings, transactions
* Fetch-forward read-only cursors, always
* Queries as objects
* Utilities for best practices (store query strings as resources)

#### Examples
A full set of examples is being developed in [jdbj-examples](https://github.com/randyp/jdbj-examples), but here are some in this repository.

* [Example Main](src/test/java/com/github/randyp/jdbj/example/InformationSchemaMain.java)
* [Example DAO (connection as construction parameter)](src/test/java/com/github/randyp/jdbj/example/StudentDAO.java)

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
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "specify query, specify results mapper, bind parameters, execute query". We get to reuse steps 1-2 across most requests and only do the minimum amount of query building for each request.

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
mvn release:clean release:prepare -B && mvn release:perform && git push
```

#### 0.1.3 Todo
* [ ] Bind Optionals, checks that value is defaulted if not present
* [ ] Extensibility

#### 0.1.4 Todo
* [ ] Allowed to bind defaults during Map query phase, is passed to phase 3 execute query
* [ ] Mock coverage for jdbc's unsupported features

#### 0.1.5 Todo
* [ ] Hacky-yet-compatible batch insert query
* [ ] Java Doc
* [ ] Examples

#### 0.1.6
* [ ] Somehow remove most antlr classes we don't need from shading process...

