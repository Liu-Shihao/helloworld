FROM adoptopenjdk/openjdk8
RUN pwd
RUN ls
COPY target/*.jar /usr/local/app.jar
WORKDIR /usr/local
CMD java -jar app.jar
