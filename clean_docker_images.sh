#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

CONTAINER_NAME=$1
TAG=$2
IMAGE_NAME=$3

docker stop ${CONTAINER_NAME} || true

docker rm ${CONTAINER_NAME} || true

docker rmi ${IMAGE_NAME}:${TAG}
