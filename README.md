[![Build Status](https://travis-ci.org/blstream/TOZ_BE.svg?branch=master)](https://travis-ci.org/blstream/TOZ_BE)

# TOZ backend

TODO [short description](https://en.support.wordpress.com/markdown-quick-reference/)

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

## Slack (private)

[Contact us](https://patronage-2017.slack.com/messages/backend)
[Developer news](https://patronage-2017.slack.com/messages/backend-ci/)

## Docker

It should be self explanatory. You can:

- build the docker image with: `gradle buildImage`
- create container: `gradle createContainer`
- run functional test on docker by: `gradle functionalTest` (TODO)

## Authorization

To authorize send JWT token in `Authorization` header with `Bearer ` prefix in request.

Example curl:
```
curl -X GET \
  http://localhost:8080/users \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0YjVlZmE0MC1lNjQ5LTQ3ZmQtOThkNy1jZTk3NzBlYTZlY2QiLCJlbWFpbCI6InVzZXJAbWFpbC5jb20iLCJzY29wZXMiOlsiVE9aIl0sImlhdCI6MTQ5MjE3MTY4MSwiZXhwIjoxNDkyMjU4MDgxfQ.Z3iZ3zlgV0_iZAk-iCTPr68hLBL5CvyoSHUbn3Htprc'
```

## JWT 

To configure secret for JWT add `TOZ_BE_JWT_SECRET` environmental variable with base64 encoded secret.

# Temporary server

[Heroku temporary hosting](https://vast-plains-10769.herokuapp.com/)

# Create user
curl:
```
curl -i -X POST -H "Content-Type:application/json" -d '{  "email" : "TOZ",  "password" : "admin", "forename" : "admin", "surname" : "admin","role" : "VOLUNTEER" }' http://localhost:8080/user
```
Roles :
TOZ - Organization admin.
VOLUNTEER - A Person, who is a volunteer in TOZ Organization.

