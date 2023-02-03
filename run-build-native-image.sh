#!/bin/bash

./mvnw spring-boot:build-image -Pnative

echo "you can now run the app with docker with the following:"
echo "docker run --rm -p 8080:8080 spring-boot-3-example1:0.0.1-SNAPSHOT"
