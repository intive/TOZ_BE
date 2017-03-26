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
