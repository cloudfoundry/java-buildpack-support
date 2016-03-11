#!/usr/bin/env bash

set -e

pushd java-buildpack-support
  ./mvnw -q -Dmaven.test.skip=true deploy
popd
