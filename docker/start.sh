#!/bin/bash

components=("kafka" "superset" "ignite" "druid")

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml down
    docker-compose --env-file .env -f $comp/docker-compose.yml up -d
done
