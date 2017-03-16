FROM anapsix/alpine-java
MAINTAINER Maciej Łotysz <maciej.lotysz@intive.com>
ADD build/libs/toz-core-0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#https://medium.com/@cl4r1ty/docker-spring-boot-and-java-opts-ba381c818fa2#.bvcewql21

ENTRYPOINT exec java $JAVA_OPTS \
            -Djava.security.egd=file:/dev/./urandom \
            -jar /app.jar
