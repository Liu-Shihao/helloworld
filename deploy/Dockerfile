FROM adoptopenjdk/openjdk8
CMD pwd
COPY ./target/*.jar /usr/local/app.jar
WORKDIR /usr/local
CMD java -jar app.jar
