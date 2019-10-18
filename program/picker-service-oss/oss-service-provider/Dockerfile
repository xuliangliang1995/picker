FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar oss-service-provider.jar

EXPOSE 20880

ENTRYPOINT ["java","-jar","oss-service-provider.jar"]