# Java Buildpack Support

A collection of JARs in support of the [Java Buildpack](https://github.com/cloudfoundry/java-buildpack).

## Tomcat Buildpack Support

This is a JAR which the Java buildpack places in Tomcat's `lib` directory. It contains a listener which,
when configured in Tomcat, ensures that Tomcat shuts down if an application fails to start.

The list of published versions of this JAR can be found at the following location.

| JAR | Location
| --- | ---------
| Tomcat Buildpack Support | <http://download.pivotal.io.s3.amazonaws.com/tomcat-buildpack-support/index.yml>

## Contributing

[Pull requests][] are welcome; see the [contributor guidelines][] for details.

[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: CONTRIBUTING.md

## License

The Java Buildpack Support is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
