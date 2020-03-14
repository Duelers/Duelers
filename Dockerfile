FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY ./target/Server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/duelers.jar
COPY ./resources/ /app/Server/resources
CMD java -jar duelers.jar
