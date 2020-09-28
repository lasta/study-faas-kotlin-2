# Study FaaS with Kotlin-Native 2
[1 is here](https://github.com/lasta/study-faas-kotlin).

## TODO
- [x] migrate from `build.gradle` to `build.gradle.kts`
- [x] run "Hello, World!"
  - [x] on mac
  - [x] on Amazon Linux 2
- [ ] cross-compile for platforms
  - [x] depends on host
  - [ ] at once
    - [sample][example to build at once]
- [x] mark project structure automatically by IntelliJ IDEA
- [x] clean up `build.gradle.kts`
  - [x] create `buildSrc`
    - [sample][create buildSrc]
  - [ ] unit-test in buildSrc scripts
  - [ ] generate "executables" automatically
- [x] arrange source directory structure
- [x] import JSON serializer
  - [x] use stringify
    - [`Json.encodeToSting`][kotlinx.serialization]
  - [x] use deserializer
    - [`Json.decodeFromString<Clazz>(string)`][kotlinx.serialization]
- [x] import ktor
  - [x] use HTTP Client
    - [`ktor-client-cio`][ktor-client-cio]
- [ ] import DI library
  - kodein?
- [ ] on IntelliJ IDEA
  - [x] run
  - [x] debug
  - [ ] unit test
  - [ ] sam local (AWS Serverless Application Model)
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

### Run
#### on Mac
```sh
./build/bin/native/releaseExecutable/study-faas-kotlin-2.kexe
# Hello, Kotlin/Native!
```

### on Linux (AmazonLinux 2 on Docker for mac)
```sh
docker pull gradle:latest
docker run -itd docker.io/library/gradle:latest /bin/bash
docker cp ./ $(docker ps | grep 'gradle:latest' | awk '{print $1}'):/home/gradle/faas
docker exec -it $(docker ps | grep 'gradle:latest' | awk '{print $1}') /bin/bash
# on container
cd faas
./gradlew clean build
for f in $(find build -name '*.kexe' | grep -v 'dSYM' | grep -vi 'Debug' ); do ./$f ;done
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

## Setup
### to run `sam local` on IntelliJ IDEA
### on mac
1. Install [Homebrew][Homebrew]
2. Install AWS CLI2
  * `brew install awscli`


-->

[Homebrew]: https://brew.sh/
[Installing the AWS Toolkit for JetBrains]: https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/setup-toolkit.html
