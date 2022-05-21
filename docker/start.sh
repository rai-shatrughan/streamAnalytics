#!/bin/bash

docker-compose --env-file .env -f $comp/docker-compose.yml down
docker-compose --env-file .env -f $comp/docker-compose.yml up -d

