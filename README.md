jdbj is a jdbc fluent interface for capturing query intent long before query execution

#### Why?
Other jdbc convenience libraries follow the "create, bind, execute/map" pattern that we inherited from older procedural code.
A better pattern is "build, bind, execute" where the intent of a query is captured long before

#### Guiding Principles
* no *connection handles* - just use the Connection as an argument to immutable objects
* where possible, bubble up the SQLException (not possible in Stream.tryAdvance)
* use immutable builders to capture the query intent long before query execution
* hide the PreparedStatement as much as possible during the binding phase
