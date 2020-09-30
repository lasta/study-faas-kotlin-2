# Study FaaS with Kotlin-Native 2
[1 is here](https://github.com/lasta/study-faas-kotlin).

## TODO
- [x] migrate from `build.gradle` to `build.gradle.kts`
- [x] run "Hello, World!"
  - [x] on mac
  - [x] on Amazon Linux 2
- [x] cross-compile for platforms
- [x] mark project structure automatically by IntelliJ IDEA
- [x] clean up `build.gradle.kts`
  - [x] create `buildSrc`
    - [sample][create buildSrc]
  - [ ] unit-test in buildSrc scripts
  - [ ] generate "executables" automatically
- [ ] arrange source directory structure
- [x] import JSON serializer
  - [x] use stringify
    - [`Json.encodeToSting`][kotlinx.serialization]
  - [x] use deserializer
    - [`Json.decodeFromString<Clazz>(string)`][kotlinx.serialization]
- [x] import ktor
  - [x] use HTTP Client
    - [`ktor-client-cio`][ktor-client-cio]
  - [ ] https
- [ ] import DI library
  - kodein?
- [ ] on IntelliJ IDEA
  - [x] run
  - [x] debug
  - [ ] unit test
  - ~~sam local (AWS Serverless Application Model)~~
    - runtime "provided" is unsupported
- [x] sam local (AWS Serverless Application Model)
- [ ] deploy
- [ ] create API test
  - [ ] [preacher][preacher]
- [ ] O/R mapper

[example to build at once]: https://github.com/JetBrains/kotlin/blob/1.3.20/libraries/tools/kotlin-gradle-plugin-integration-tests/src/test/resources/testProject/new-mpp-native-binaries/kotlin-dsl/build.gradle.kts
[create buildSrc]: https://www.itcowork.co.jp/blog/?p=5357
[preacher]: https://github.com/ymoch/preacher
[kotlinx.serialization]: https://github.com/Kotlin/kotlinx.serialization#introduction-and-references
[ktor-client-cio]: https://github.com/ktorio/ktor/blob/master/ktor-client/ktor-client-cio/build.gradle.kts

## TL;DR
### build
```sh
./gradlew build
```

Initially, it may take several minutes.

## Setup
### [Installing the AWS SAM CLI][Installing the AWS SAM CLI]
1. Install [Homebrew][Homebrew]
2. Install AWS CLI2
```sh
brew tap aws/tap
brew install aws-sam-cli
```

## Run
```sh
./gradlew build
cp build/bin/native/me.lasta.studyfaaskotlin2.entrypoint.withbootstrapReleaseExecutable/bootstrap.kexe sam/bootstrap
sam local start-api -t sam/template.yaml
curl http://localhost:3000/withbootstrap
```

<!-- FIXME: described below is for study-faas-kotlin (1). 
            should be adapted for 2.
### Test
#### on Mac
```console
$ ./gradlew allTests

> Configure project :
Kotlin Multiplatform Projects are an experimental feature.

BUILD SUCCESSFUL in 1s
8 actionable tasks: 2 executed, 6 up-to-date
```

## Example server
The API server to test client.

### Run the server
```console
$ ./gradlew ":etc:example:server:run"
```

See also [server's README.md](etc/example/server/README.md).
-->



[Homebrew]: https://brew.sh/
[Installing the AWS Toolkit for JetBrains]: https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/setup-toolkit.html
[Installing the AWS SAM CLI]: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html
