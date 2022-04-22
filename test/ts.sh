#!/bin/bash
start=`date`
echo "Start Time - " $start

go-wrk -c 5000 -d 10 -T 30000 http://localhost:8000/api/v1/ts

end=`date`
echo "End Time - " $end