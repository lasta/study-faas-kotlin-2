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
- [ ] import ktor
  - [ ] use HTTP Client
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
docker pull amazonlinux:latest
docker run -itd amazonlinux:latest /bin/bash
docker cp build/bin/native/releaseExecutable/study-faas-kotlin-2.kexe $(docker ps | grep 'amazonlinux:latest' | awk '{print $1}'):/root/study-faas-kotlin-2.kexe
docker exec -it $(docker ps | grep 'amazonlinux:latest' | awk '{print $1}') /root/study-faas-kotlin-2.kexe
# Hello, Kotlin/Native!
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
