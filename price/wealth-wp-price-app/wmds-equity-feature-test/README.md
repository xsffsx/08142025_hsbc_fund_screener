## wmds-equity-feature-test
> wmds-equity-feature-test is a module for quick regression testing
>

### How to run:

1.use maven command line in local
```
mvn clean verify -Dunit-test.skip=true -B -U
```
or

2.run by `CucumberIT` java class in IDEA

3.run with jenkins pipeline
```
https://jenkins-custom-glwm01.digital-tools.euw1.prod.aws.cloud.hhhh/job/WEALTH-PLATFORM/job/Portfolio%20Management/job/build-wealth-wp-price-feature-test/
```

### How to do development:

1.add test scenarios in feature file `resources/features`

2.implement feature file step with Java code

