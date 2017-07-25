[![Build Status](https://travis-ci.org/blstream/TOZ_BE.svg?branch=master)](https://travis-ci.org/blstream/TOZ_BE)

# TOZ backend

*Note: This project is frozen. If you'd like to continue its development I encourage you to do fork.*

## Prerequisites

* [Java 1.8 (Oracle)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Gradle](https://docs.gradle.org/current/userguide/installation.html)

## Build with gradle wrapper

    ./gradlew build

[Documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html)

## Run in place (monitor changes)

    ./gradlew bootRun

## REST API documentation

[Production](http://patronage2017.blstream.com/swagger-ui.html)
[Testbed](http://testbed.patronage2017.blstream.com/swagger-ui.html)
[Bleeding edge](http://dev.patronage2017.blstream.com/swagger-ui.html)

## Docker

It should be self explanatory. You can:

- build the docker image with: `gradle buildImage`
- create container: `gradle createContainer`
- run functional test on docker by: `gradle functionalTest` (TODO)

## Authorization

To acquire token `POST` credentials to `/tokens/acquire`

Example curl:
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
    "email": "user@mail.com",
    "password": "P4ssw0rd" 
}' 'http://localhost:8080/tokens/acquire'
```

To authorize send JWT token in `Authorization` header with `Bearer ` prefix in request.

Example curl:
```
curl http://localhost:8080/users \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer eyJhb...'
```

## JWT 

To configure secret for JWT add `TOZ_BE_JWT_SECRET` environmental variable with base64 encoded secret.

# Super admin
Set `TOZ_BE_SA_PASSWORD` environmental variable with super admin password before starting server.
This password will be used to initialize super admin database entity.

# Create user

curl:
```
curl -X POST -H 'Content-Type: application/json' \
     -u SA:$TOZ_BE_SA_PASSWORD \
     -d '{  "name": "Johny",
            "password": "johnySecret",
            "surname":"Bravo",
            "phoneNumber":"111222333",
            "email": "johny.bravo@gmail.com",
            "roles": ["VOLUNTEER"]}' \
     http://localhost:8080/admin/users
```

## User roles

* SA - Super admin
* TOZ - Organization admin
* VOLUNTEER - A Person, who is a volunteer in TOZ Organization
