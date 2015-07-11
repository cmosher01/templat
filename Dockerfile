FROM java

MAINTAINER Christopher A. Mosher <cmosher01@gmail.com>

RUN apt-get update && apt-get install --no-install-recommends -y \
    nginx \
    supervisor

# supervisor
CMD ["supervisord"]
COPY supervisord.conf /etc/supervisor/conf.d/

# nginx
EXPOSE 80
COPY nginx.conf /etc/nginx/
COPY maven.nginx /etc/nginx/sites-available/
RUN rm /etc/nginx/sites-enabled/default
RUN ln -s /etc/nginx/sites-available/maven.nginx /etc/nginx/sites-enabled/maven.nginx



WORKDIR /root/

# install wrapper and download into cache
COPY gradle/ ./
RUN ./gradlew --dry-run

COPY settings.gradle ./
COPY build.gradle ./
COPY src/ ./src/

RUN mkdir -p ./build/reports/logs/
RUN ./gradlew build javadoc asciidoc install 2>&1 | tee ./build/reports/logs/build.log

RUN ln -s /root/.m2/repository ./build/asciidoc/html5/maven
RUN ln -s . ./build/asciidoc/html5/templat
