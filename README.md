# Java Buildpack Support
[![Build Status](https://travis-ci.org/cloudfoundry/java-buildpack-support.png?branch=master)](https://travis-ci.org/cloudfoundry/java-buildpack-support)

A collection of JARs in support of the [Java Buildpack](https://github.com/cloudfoundry/java-buildpack).

## Tomcat Buildpack Support

This is a JAR which the Java buildpack places in Tomcat's `lib` directory. It contains a listener which,
when configured in Tomcat, ensures that Tomcat shuts down if an application fails to start.

## Building

This project is built with Gradle. To build the JARs:

```bash
./gradlew
```

To publish the JARs to Amazon S3, use the `publish` target:

```bash
./gradlew publish
```

## Running Tests
To run the tests, do the following:

```bash
./gradlew
```

## Contributing
[Pull requests][] are welcome; see the [contributor guidelines][] for details.

[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: CONTRIBUTING.md

## License
The Tomcat Builder is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
