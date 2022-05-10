#!/bin/bash

components=("ws" "kafka" "cassandra" "prometheus" "grafana" "fluentd")

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml down
    docker-compose --env-file .env -f $comp/docker-compose.yml up -d
done

echo "sleep for 60 sec : cassandra Instance to be ready for Jeager"
sleep 60
docker-compose --env-file .env -f jaeger/docker-compose.yml down
docker-compose --env-file .env -f jaeger/docker-compose.yml up -d