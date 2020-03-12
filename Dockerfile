FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY ./Server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/duelers.jar
CMD java -jar duelers.jar
