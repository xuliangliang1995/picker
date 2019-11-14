FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar wechat-service-provider.jar

EXPOSE 20880

ENTRYPOINT ["java","-jar","wechat-service-provider.jar"]