# Task 1 Reporting Structure

It seemed like using a database-side solution to calculate the total number of Direct Reports made the most sense.  With my limited knowledge of MongoDB, after looking at the documentation, `$graphLookup` seemed like the best option.  I started by setting up a local MongoDB with data to test out building the aggregation pipeline query.  After coming up with a query that worked, I started translating the native query into the sprint data compatible java code. After some trial and error, I ended up discovering my query worked against a standard MongoDB install, but not against the java embedded version.  After some research I swapped out `mongo-java-server` for `de.flapdoodle.embed:de.flapdoodle.embed.mongo` which does not have this issue.


# Task 2 Compensation

I Initially considered whether this new endpoint should return an employee's current compensation, or the history of their compensation.  Since it seems like returning historical compensation was more of a admin use case, I chose to implement only returning the employee's current compensation.  My next choice was whether this belonged in the `employee` collection or as a separate collection.  In a traditional RDBMS, this would be an extra table with `(user_id, salary, effective_date)` fields. However in a document store like MongoDB, it seems like the general advice is to include this sort of information in embedded documents.

# Misc

I **think** I discovered an existing bug with the "update employee" endpoint which would cause duplicate records to be created due to the absence of an annotated `@Id` on the `Employee` entity. The method in `AggregateRepository` probably should be in `EmployeeRepository` instead, but I could not determine how to refactor that interface into a concrete class while still allowing spring to auto-proxy the `findByEmployeeId` method. 

This was built and tested against JDK13

```
$ java --version
openjdk 13.0.14 2023-01-17
OpenJDK Runtime Environment Zulu13.54+17-CA (build 13.0.14+5-MTS)
OpenJDK 64-Bit Server VM Zulu13.54+17-CA (build 13.0.14+5-MTS, mixed mode)
```