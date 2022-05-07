#!/bin/bash

#mvnw compile jib:dockerBuild
#mvnw compile jib:buildTar
#mvnw exec:java

bash gradlew jibDockerBuild

cd ../docker

docker-compose -f ws/docker-compose.yml up -d

cd ../ws

#bash gradlew vertxRun
