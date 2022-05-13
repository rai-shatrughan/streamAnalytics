#!/bin/bash

export JVM_ARGS="-Xms1g -Xmx4g"

JVM_ARGS="-Xms1g -Xmx4g" jmeter -n -t ts.jmx -l results.jtl
