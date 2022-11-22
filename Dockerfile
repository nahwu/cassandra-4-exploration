#FROM amazoncorretto:8u352-alpine3.16-jre
FROM amazoncorretto:17.0.5-alpine3.16

LABEL maintainer="nahwu"
COPY /target/cassandra-4-exploration-0.0.1-SNAPSHOT.jar  /var/tmp/my-app/
RUN chmod -R 700 /var/tmp/my-app/cassandra-4-exploration-0.0.1-SNAPSHOT.jar
WORKDIR /var/tmp/my-app

EXPOSE 8002

CMD ["java", "-jar", "cassandra-4-exploration-0.0.1-SNAPSHOT.jar"]
