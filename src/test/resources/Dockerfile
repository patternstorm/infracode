FROM ubuntu:latest

RUN apt-get update && apt-get upgrade -y

RUN apt-get install nginx -y

EXPOSE 80

ENTRYPOINT ["nginx"]

CMD ["-g", "'daemon off;'"]