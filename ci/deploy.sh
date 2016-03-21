#!/usr/bin/env sh

set -e

cd java-buildpack-support
./mvnw -q -Dmaven.test.skip=true deploy
