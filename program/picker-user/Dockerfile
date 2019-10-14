FROM java:8

MAINTAINER 许良良

#VOLUME /data/program

ADD target/*.jar picker-user.jar

EXPOSE 10001 20880

ENTRYPOINT ["java","-jar","picker-user.jar"]