There are 4 phases to executing a static query with dynamic input

1) Provide query string which uses named parameters - captured as static methods on JDBJ
2) Provide strategy to retrieving results from query - captured as classes returned by Phase 1 methods
3) Provide named parameter bindings - captured as *eventually* classes returned by Phase 2 classes
4) Provide connection/data source - captured as instance methods on Phase 3 classes
