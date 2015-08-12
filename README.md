jdbj is a jdbc fluent interface for capturing query intent long before query execution

#### Why?
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "specify query, specify results mapper, bind parameters, execute query". We get to reuse steps 1-2 across all requests and only do the minimum amount of query building for each request.

#### Guiding Principles
* no *connection handles* - just use the Connection as an argument to immutable objects
* where possible, bubble up the SQLException (not possible in Stream.tryAdvance)
* use immutable builders to capture the query intent long before query execution
* hide the PreparedStatement as much as possible during the binding phase
