#!/bin/bash

echo "GraalVM 22.3+ must be present and libfreetype6-dev installed in the system"

./mvnw native:compile -Pnative

echo ""
echo "the executable has been build in /target"
