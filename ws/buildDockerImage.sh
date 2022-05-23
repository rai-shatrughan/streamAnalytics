#!/bin/bash

#mvnw compile jib:dockerBuild
#mvnw compile jib:buildTar
#mvnw exec:java

bash gradlew clean jibDockerBuild \
    -Djib.from.image=docker://eclipse-temurin:11-jre \
    -Djib.to.image=sr-me-kafcas \
    -Djib.to.tags=v1 \
    -Djib.container.mainClass=sr.me.ws.CustomLauncher \
    -Djib.container.args="run,sr.me.ws.KafkaCassandraVerticle,-DJAEGER_AGENT_HOST=172.18.0.91" \
    -Djib.container.creationTime=USE_CURRENT_TIMESTAMP

bash gradlew jibDockerBuild

cd ../docker

docker-compose -f docker-compose.yml up -d sr-me-kafcas sr-me-ws

cd ../ws

#bash gradlew vertxRun
