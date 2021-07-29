FROM maven:3-jdk-11 as LOGGER
COPY ../logger /usr/src/app/
RUN mvn --batch-mode -f /usr/src/app/pom.xml clean install package -DskipTests

FROM maven:3-jdk-11 as COMMON
COPY ../common /usr/src/app/
RUN mvn --batch-mode -f /usr/src/app/pom.xml clean install package -DskipTests

FROM maven:3-jdk-11 as BUILD

COPY ../users /usr/src/app
COPY --from=LOGGER /usr/src/app/target /usr/src/app/logger
COPY --from=COMMON /usr/src/app/target /usr/src/app/common
RUN mvn install:install-file -Dfile=/usr/src/app/logger/logger-0.0.1-SNAPSHOT.jar -DgroupId=fr.esgi -DartifactId=logger -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
RUN mvn install:install-file -Dfile=/usr/src/app/common/common-0.0.1-SNAPSHOT.jar -DgroupId=fr.esgi -DartifactId=common -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
RUN mvn --batch-mode -f /usr/src/app/pom.xml  package -DskipTests

FROM openjdk:11-jre-slim
ENV PORT 3002
EXPOSE 3002
COPY --from=BUILD /usr/src/app/target /opt/target
WORKDIR /opt/target

CMD ["/bin/bash", "-c", "find -type f -name '*.jar' | xargs java -jar"]