FROM gradle:alpine

MAINTAINER Christopher A. Mosher <cmosher01@gmail.com>

ENTRYPOINT ["gradle"]
CMD ["build"]

USER gradle
ENV GRADLE_OPTS -Dorg.gradle.daemon=false
WORKDIR /home/gradle

COPY settings.gradle ./
COPY build.gradle ./
COPY src/ ./src/
