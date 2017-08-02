# Base image from Spring Docker tutorial
FROM frolvlad/alpine-oraclejdk8:slim
ARG tmcappid
ARG tmcsecret

VOLUME /tmp

# Add the jar and the properties to the working directory of the container
ADD build/libs/tdd-pingpong-0.1.0.jar app.jar
ADD application.properties .


# Static files, such as index.html, supposedly need a modification time, which can be set by touching
#RUN export TMC_APP_ID=${tmcappid}
#RUN export TMC_SECRET=${tmcsecret}
RUN sh -c 'touch /app.jar'

# options for running java
ENV JAVA_OPTS="-Dspring.profiles.active=prod -DTMC_APP_ID=${tmcappid} -DTMC_SECRET=${tmcsecret}"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
