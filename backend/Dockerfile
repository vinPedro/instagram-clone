FROM maven:3.9.9-amazoncorretto-23
LABEL maintainer="paulopereira.dev"

EXPOSE 8080

RUN mkdir -p /home/instagram

COPY ./instagram /home/instagram

WORKDIR /home/instagram
