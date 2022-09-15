FROM  openjdk:17
ARG JAR_FILE=target/Beck-end_Ya-disk-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} beck-end_ya-disk-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","beck-end_ya-disk-0.0.1-SNAPSHOT.jar"]
