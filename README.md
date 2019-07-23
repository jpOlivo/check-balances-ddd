# Getting Started

Alternative implementation of [checks-balances](https://github.com/jpOlivo/check-balances) using Domain-driven design (DDD).

### Reference Documentation
For further reference, please consider the following sections:

#### Design

The design of this API is based on concepts of DDD. 

#### API Rest

The API documentation can be found [here](http://localhost:8080/swagger-ui.html#)


#### Tests

A set of unit tests can be found under */src/test/java*

Use 'mvn test' for execute the unit tests  

`$ mvn test`


#### Running the API

There is two alternatives, both depend maven.

1. From Docker

`sudo docker run -p 8080:8080 jpolivo/checks-balances-ddd`

2. From Spring Boot Maven plugin

`$ mvn spring-boot:run`

3. From Java (it requires Spring Boot Maven plugin to create an executable jar)

`$ java -jar target/checks-balances-ddd-0.0.1-SNAPSHOT.jar `
