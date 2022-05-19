go run main.go -c 1 -d 1 -T 100 -M PUT \
    -H "Content-Type: application/json" \
    -body {"name":"sr"} \
    http://172.18.0.21:8000/api/v1/ts