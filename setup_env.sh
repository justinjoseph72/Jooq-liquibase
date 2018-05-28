#!/bin/bash
set +e
IFS=$'\n\t'


DB_IMAGE_NAME=yc_postgres_img
DB_TAG=latest
DB_CONTAINER_NAME=yc_postgres_cont

echo 'cleaning postgress images'
./clean_docker_images.sh ${DB_CONTAINER_NAME} ${DB_TAG} ${DB_IMAGE_NAME}

sleep 5

echo 'starting docker containers and images for postgres database'
./start_db.sh ${DB_IMAGE_NAME} ${DB_TAG} ${DB_CONTAINER_NAME}

