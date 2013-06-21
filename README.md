# Java Buildpack Support
[![Build Status](https://travis-ci.org/cloudfoundry/java-buildpack-support.png?branch=master)](https://travis-ci.org/cloudfoundry/java-buildpack-support)

A collection of JARs in support of the Java Buildpack.

## Building

This project is built with Gradle. To build the JARs:

	./gradlew

To publish the JARs to Amazon S3, use the `publish` target:

	./gradlew publish

## Tomcat Buildpack Support

This is a JAR which the Java buildpack places in Tomcat's `lib` directory. It contains a listener which,
when configured in Tomcat, ensures that Tomcat shuts down if an application fails to start.
