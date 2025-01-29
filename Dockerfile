# Use an official OpenJDK image as the base image
FROM container-registry.oracle.com/java/jdk-no-fee-term:21 AS build
#FROM openjdk:23-jdk as build

# Set the working directory inside the container
WORKDIR /usr/share
RUN set -x && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar -xvf apache-maven-*-bin.tar.gz  && \
    rm apache-maven-*-bin.tar.gz && \
    mv apache-maven-* maven && \
    ln -s /usr/share/maven/bin/mvn /bin/

WORKDIR cronparser/
ADD src src
ADD pom.xml .
RUN mvn package

#CMD ["java", "-jar", "target/cronparser-1.0-SNAPSHOT.jar", "*/15 0 1,15 * 1-5 /usr/bin/find"]


