#!/bin/bash

components=("ws" "kafka" "cassandra" "prometheus" "grafana" "jaeger-cassandra-schema" "jaeger")

for comp in ${components[@]}; do
    docker-compose --env-file .env -f $comp/docker-compose.yml down
    docker-compose --env-file .env -f $comp/docker-compose.yml up -d
done
