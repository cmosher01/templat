FROM maven

MAINTAINER Christopher A. Mosher <cmosher01@gmail.com>

RUN apt-get update && apt-get install --no-install-recommends -y \
	asciidoc \
	make \
	nginx \
	source-highlight \
	supervisor

WORKDIR /root/

COPY settings.xml ./.m2/

COPY Makefile ./
COPY pom.xml ./
COPY src/ ./src/
COPY doc/ ./doc/

COPY nginx.conf /etc/nginx/
COPY maven.nginx /etc/nginx/sites-available/
RUN rm /etc/nginx/sites-enabled/default
RUN ln -s /etc/nginx/sites-available/maven.nginx /etc/nginx/sites-enabled/maven.nginx

RUN make 2>&1 | tee build.log

EXPOSE 80

COPY supervisord.conf /etc/supervisor/conf.d/
CMD ["supervisord"]

RUN ln -s /root/.m2/repository /root/doc/site/maven
RUN ln -s . /root/doc/site/templat
