#!/bin/bash
start=`date`
echo "Start Time - " $start

go-wrk -c 8000 -d 10 -T 30000 http://172.18.0.21:8000/api/v1/ts

end=`date`
echo "End Time - " $end