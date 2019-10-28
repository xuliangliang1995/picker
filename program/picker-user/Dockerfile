FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar picker-user.jar

EXPOSE 10001

ENTRYPOINT ["java","-jar","picker-user.jar"]

# docker -build -t picker-user .
# docker run -d -p 10001:10001 --name picker-user picker-user