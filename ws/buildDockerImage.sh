#!/bin/bash

#mvnw compile jib:dockerBuild
#mvnw compile jib:buildTar
#mvnw exec:java

bash gradlew jibDockerBuild
