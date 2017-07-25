# Base image from Spring Docker tutorial
FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

# Add the jar to the working directory of the container
ADD build/libs/tdd-pingpong-0.1.0.jar app.jar

# Static files, such as index.html, supposedly need a modification time, which can be set by touching
RUN sh -c 'touch /app.jar'

# options for running java
ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
