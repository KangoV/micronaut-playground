FROM openjdk:17-jdk-alpine
COPY build/libs/db-*-all.jar db.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar db.jar