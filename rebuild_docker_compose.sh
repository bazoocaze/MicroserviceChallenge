#!/bin/bash

mvn package
docker-compose down
docker-compose build
docker-compose up

