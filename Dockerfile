FROM openjdk:17-jdk-alpine
COPY build/libs/playground-micronaut-*-all.jar playground.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar playground.jar