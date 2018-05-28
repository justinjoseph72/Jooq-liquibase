#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

IMAGE_NAME=$1
TAG=$2
CONTAINER_NAME=$3

(cd db ; docker build -t ${IMAGE_NAME}:${TAG} .)

docker run -p 5432:5432 --name ${CONTAINER_NAME} -v $HOME/yc_pg_data:/var/lib/postgresql/connectionData -d ${IMAGE_NAME}:${TAG}
