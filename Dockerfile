FROM java

MAINTAINER Christopher A. Mosher <cmosher01@gmail.com>

RUN apt-get update && apt-get install --no-install-recommends -y \
	nginx \
    supervisor

CMD ["supervisord"]
COPY supervisord.conf /etc/supervisor/conf.d/

EXPOSE 80
COPY nginx.conf /etc/nginx/
COPY maven.nginx /etc/nginx/sites-available/
RUN rm /etc/nginx/sites-enabled/default
RUN ln -s /etc/nginx/sites-available/maven.nginx /etc/nginx/sites-enabled/maven.nginx

WORKDIR /root/

COPY gradle/ ./
COPY settings.gradle ./
COPY build.gradle ./

RUN ./gradlew clean

COPY src/ ./src/

RUN mkdir -p build/reports/logs/
RUN ./gradlew components 2>&1 | tee build/reports/logs/components.log
RUN ./gradlew dependencies 2>&1 | tee build/reports/logs/dependencies.log
RUN ./gradlew asciidoc 2>&1 | tee build/reports/logs/asciidoc.log
RUN ln -s /root/.m2/repository build/asciidoc/html5/maven
RUN ln -s . build/asciidoc/html5/templat
RUN ./gradlew build javadoc install 2>&1 | tee build/reports/logs/build.log
