#!/usr/bin/env sh

set -e -u

ln -fs $PWD/maven $HOME/.m2

cd java-buildpack-support
./mvnw -q package
