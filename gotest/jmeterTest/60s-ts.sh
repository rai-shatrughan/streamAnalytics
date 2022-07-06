#!/bin/bash
start=`date`
echo "Start Time - " $start

s=`date -u +%S`
v1=$(($s + 10))

json="{\"name\":\"sr_go_furn1\",\"timestamp\": \"`date -u +\"%Y-%m-%dT%H:%M:%S.%sZ\"`\",\"property\": \"temperature\",\"unit\": \"celcius\",\"value\": $v1}"

go-wrk -c 8000 -d 60 -T 30000 -M PUT \
    -H "Content-Type: application/json" \
    -body "$json" \
    http://172.18.0.21:8000/api/v1/ts 


end=`date`
echo "End Time - " $end
