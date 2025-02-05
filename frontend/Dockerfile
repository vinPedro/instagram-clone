FROM node:23-bookworm-slim
LABEL maintainer="paulopereira.dev"

EXPOSE 3000

RUN mkdir -p /home/instagram

WORKDIR /home/instagram

COPY ./instagram/package.json /home/instagram/package.json
COPY ./instagram/package-lock.json /home/instagram/package-lock.json

RUN npm install

COPY ./instagram /home/instagram
