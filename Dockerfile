FROM frolvlad/alpine-oraclejdk8:slim
# Accept two arguments for injecting the TMC credentials in the container
ARG tmcappid
ARG tmcsecret

# Mounts a directory on the host filesystem for the container
VOLUME /tmp

# Add the jar and the properties to the working directory of the container
COPY build/libs/tdd-pingpong.jar app.jar
COPY config config
COPY tmc-assets tmc-assets

# Static files, such as index.html, supposedly need a modification time, which are set when created by touch
RUN sh -c 'touch /app.jar'

# Options for running java. Sets the profile as 'production' and adds the TMC credentials as environment variables
ENV JAVA_OPTS="-Dspring.profiles.active=dev -DTMC_APP_ID=${tmcappid} -DTMC_SECRET=${tmcsecret}"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
