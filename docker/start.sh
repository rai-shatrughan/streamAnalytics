#!/bin/bash

docker-compose --env-file .env -f docker-compose.yml down
docker-compose --env-file .env -f docker-compose.yml up -d

#docker ps -a --format "table {{.ID}} \t {{.Names}} \t {{.State}} "

