FROM maven

MAINTAINER Christopher A. Mosher <cmosher01@gmail.com>

RUN apt-get update && apt-get install --no-install-recommends -y \
	asciidoc \
	make

WORKDIR /root/

COPY settings.xml ./.m2/

COPY Makefile ./
COPY pom.xml ./
COPY src/ ./src/
COPY doc/ ./doc/

RUN make >build.log 2>&1
