FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar blog-service-provider.jar

EXPOSE 20880

ENTRYPOINT ["java","-jar","blog-service-provider.jar"]