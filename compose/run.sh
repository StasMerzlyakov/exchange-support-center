#!/bin/bash
#

rm -rf minio/data
mkdir -p minio/data

docker compose up
docker-compose rm -fsv


