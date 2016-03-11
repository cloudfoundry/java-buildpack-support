#!/usr/bin/env bash

set -e

pushd java-buildpack-support
  ./mvnw -q package
popd
