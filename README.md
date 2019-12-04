# visrec-tck
Test Compability Kit for Visual Recognition API. A TCK is used to validate an implementation of the visrec-api by running predefined tests.

The TCK comes with an audit file and the tests. The audit file is used for the API-development team to come up with a specification for the tests
and then implement those tests. A TCK coverage report tells if the audit file is implemented as tests for implementors. 

### How to generate a TCK coverage report
`mvn clean install -DcreateTCKReport=true`
