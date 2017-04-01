[![Build Status](https://travis-ci.org/blstream/TOZ_BE.svg?branch=master)](https://travis-ci.org/blstream/TOZ_BE)

# TOZ backend

TODO [short description](https://en.support.wordpress.com/markdown-quick-reference/)

## Prerequisites

* [Java 1.8 (Oracle)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Gradle](https://docs.gradle.org/current/userguide/installation.html)

## Build with gradle

    gradle build

[Documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html)

## Run in place (monitor changes)

    gradle bootRun

## REST API documentation

    http://localhost:8080/swagger-ui.html

## Slack notifications (private)

[Link](https://patronage-2017.slack.com/messages/backend-ci/)

## Docker

It should be self explanatory. You can:

- build the docker image with: `gradle buildImage`
- create container: `gradle createContainer`
- run functional test on docker by: `gradle functionalTest` (TODO)

# Temporary server

[Heroku temporary hosting](https://vast-plains-10769.herokuapp.com/)

# Create user
curl:
```
curl -i -X POST -H "Content-Type:application/json" -d '{  "username" : "TOZ",  "password" : "admin", "forename" : "admin", "surname" : "admin","role" : "VOLUNTEER" }' http://localhost:8080/user
```
Roles :
TOZ - Organization admin.
VOLUNTEER - A Person, who is a volunteer in TOZ Organization.

