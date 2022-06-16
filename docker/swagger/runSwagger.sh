docker pull swaggerapi/swagger-editor
docker stop swag
docker rm swag
docker run  --name swag \
            -p "8080:8080" \
            swaggerapi/swagger-editor