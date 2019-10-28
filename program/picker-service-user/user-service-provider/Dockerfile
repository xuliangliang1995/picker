FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar user-service-provider.jar

EXPOSE 20880

ENTRYPOINT ["java","-jar","user-service-provider.jar"]