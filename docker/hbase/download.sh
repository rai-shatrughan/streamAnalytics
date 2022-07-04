
#!/bin/bash
HBASE_VERSION=2.4.12
FILE=./binary/hbase-${HBASE_VERSION}-bin.tar.gz
if test -f "$FILE"; then
    echo "$FILE exists."
else
    mkdir -p binary
    cd binary
    wget https://archive.apache.org/dist/hbase/${HBASE_VERSION}/hbase-${HBASE_VERSION}-bin.tar.gz
fi