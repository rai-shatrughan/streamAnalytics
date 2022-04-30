#!/bin/bash
start=`date`
echo "Start Time - " $start

s=`date -u +%S`
v1=$(($s + 10))
v2=$(($s + 50))

json="[{\"timestamp\": \"`date -u +\"%Y-%m-%dT%H:%M:%SZ\"`\",\"property\": \"temperature\",\"unit\": \"celcius\",\"value\": $v1},{\"timestamp\": \"`date -u +\"%Y-%m-%dT%H:%M:%SZ\"`\",\"property\": \"pressure\",\"unit\": \"ton\",\"value\": $v2}]"

go-wrk -c 8000 -d 3600 -T 30000 -M PUT \
    -H "Content-Type: application/json" \
    -body "$json" \
    http://172.18.0.21:8000/api/v1/ts 


end=`date`
echo "End Time - " $end