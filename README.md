# visrec-tck
Test Compability Kit for Visual Recognition API. A TCK is used to validate an implementation of the visrec-api by running predefined tests.

The TCK comes with an audit file and the tests. The audit file is used for the API-development team to come up with a specification for the tests
and then implement those tests. A TCK coverage report tells if the audit file is implemented as tests for implementors. 

### How to generate a TCK coverage report
`mvn clean compile -DcreateTCKReport=true`

And then open the report: `target/coverage-JSR 381.html`

## How to run the TCK against any implementation
Copy `JavaVisRec/jsr381-tck-ri` repository, change the implementation in the pom and replace the configuration
with how you need it to run.

Make sure to run `mvn install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip` to install
this dependency locally.
