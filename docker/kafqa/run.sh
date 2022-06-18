docker pull gojektech/kafqa

docker stop kafqa
docker rm kafqa
docker run  --name kafqa \
            -p "8080:8080" \
            gojektech/kafqa