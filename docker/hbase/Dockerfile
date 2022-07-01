FROM openjdk:8

ARG HBASE_VERSION=2.4.12
ARG HBASE_DIR=hbase-${HBASE_VERSION}
ARG HBASE_BINARY=hbase-${HBASE_VERSION}-bin.tar.gz

ENV JAVA_HOME /usr/local/openjdk-8/
ENV PATH $JAVA_HOME/bin:$PATH

VOLUME /tmp
VOLUME /logs
RUN mkdir -p /tmp/zookeeper

ADD binary/${HBASE_BINARY} /opt/

ENV PATH=$PATH:/opt/${HBASE_DIR}/bin
ENV HBASE_HOME=/opt/${HBASE_DIR}

ADD conf/hbase-env.sh /opt/${HBASE_DIR}/conf/hbase-env.sh
ADD conf/hbase-site.xml /opt/${HBASE_DIR}/conf/hbase-site.xml

WORKDIR /opt/${HBASE_DIR}