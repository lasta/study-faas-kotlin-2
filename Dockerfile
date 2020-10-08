FROM amazonlinux:2
RUN yum -y install tar gcc gcc-c++ make ncurses-compat-libs
# for curl
RUN yum -y install libcurl-devel openssl-devel
RUN amazon-linux-extras enable corretto8
RUN yum clean metadata
# for gradle
RUN yum -y install java-1.8.0-amazon-corretto-devel
RUN yum -y install install which zip unzip
RUN curl -s http://get.sdkman.io | bash && \
    bash ${HOME}/.sdkman/bin/sdkman-init.sh && \
    source ${HOME}/.bashrc && \
    sdk install gradle

# cmake for sentry
RUN yum -y install autoconf automake freetype-devel git libtool mercurial pkgconfig zlib-devel wget
RUN wget https://cmake.org/files/v3.18/cmake-3.18.3.tar.gz && tar xvzf cmake-3.18.3.tar.gz && cd cmake-3.18.3 && ./bootstrap && make && make install

# sentry
RUN mkdir -p /root/sentry && cd /root/sentry/ && \
    wget https://github.com/getsentry/sentry-native/releases/download/0.4.2/sentry-native.zip && \
    mkdir build && \
    unzip sentry-native.zip -d build && \
    cd build && \
    cmake -B build && \
    cmake --build build -j8
RUN mkdir -p /usr/local/lib64 && cd /usr/local/lib64 && \
    ln -s /root/sentry/build/build/libsentry.so && \
    echo "/usr/local/lib64" > /etc/ld.so.conf.d/usr-local-lib.conf
RUN echo "/root/sentry/build/build" > /etc/ld.so.conf.d/sentry.conf && ldconfig

