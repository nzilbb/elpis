# elpis-java

Java client library for programmatically accessing functions of the
[Elpis](https://elpis.net.au/) transcription acceleration system.

Elpis is a tool to obtain a first-pass automatic transcription on untranscribed
      audio, for manual correction.</p>

Elpis was developed by the
[Centre of Excellence for the Dynamics of Language (CoEDL)](https://www.dynamicsoflanguage.edu.au/)
as part of the *Transcription Acceleration Project* (TAP) cross-disciplinary
project aimed at identifying the workflows of linguists and language workers during
transcription, and developing assistive tools to accelerate that process.

It has a user-friendly browser-based interface, which can be used by researchers
to upload training data, initiate model training, and provide recordings for
automatic transcription. This GUI uses an API which is documented
[here](https://github.com/nzilbb/elpis-java/blob/master/docs/README.md)

This **elpis-java** client library is designed to also integrate with the Elpis API, and
was develped by the
[New Zealand Institute of Language, Brain and Behaviour](https://www.canterbury.ac.nz/nzilbb/)
(NZILBB)

## Documentation

Detailed API documentation is available [here](https://nzilbb.github.io/elpis-java/)

## Prerequisites

- [Apache ant](https://ant.apache.org/) for building
- An Elpis server for running unit tests, and/or doing training/transcription

## Build targets

- `ant` - builds the `bin/nzilbb.elpis.jar` library
- `ant test` - runs unit tests, which requires a running Elpis server to work; you
   must set the URL in the unit test files in nzilbb/elpis/test/ 
- `ant javadoc` - produces JavaDoc API documentation.

## Version information

To discover the version of <i>nzilbb.elpis.jar</i> you have:

```
unzip -z nzilbb.elpis.jar
```