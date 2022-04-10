#!/bin/bash

components=("druid" "superset" "ignite")

for comp in ${components[@]}; do
    docker-compose -f $comp/docker-compose.yml down
    docker-compose -f $comp/docker-compose.yml up -d
done
