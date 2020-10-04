FROM amazonlinux:2
RUN yum -y install tar gcc gcc-c++ make ncurses-compat-libs
RUN yum -y install libcurl-devel openssl-devel
RUN amazon-linux-extras enable corretto8
RUN yum clean metadata
RUN yum -y install java-1.8.0-amazon-corretto-devel
RUN yum -y install install which zip unzip
RUN curl -s http://get.sdkman.io | bash && \
    sh /root/.sdkman/bin/sdkman-init.sh && \
    source /root/.bashrc && \
    sdk install gradle
