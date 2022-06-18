#!/bin/bash

access_token=$(curl -X POST -H 'Content-type:application/json' \
-H 'accept: application/json' \
--data-binary '{
  "password": "admin",
  "provider": "db",
  "refresh": true,
  "username": "admin"
}' http://172.18.0.140:8088/api/v1/security/login | jq -r '.access_token')

echo $access_token

csrf_token=$(curl -X GET \
-H 'accept: application/json' \
-H "Authorization: Bearer $access_token" \
 http://172.18.0.140:8088/api/v1/security/csrf_token/ | jq -r '.result')
  
echo $csrf_token

curl -X 'POST'\
 'http://172.18.0.140:8088/api/v1/database/' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-H "Authorization: Bearer $access_token" \
-d '{
  "allow_ctas": true,
  "allow_cvas": true,
  "allow_dml": true,
  "allow_file_upload": true,
  "allow_multi_schema_metadata_fetch": true,
  "allow_run_async": true,
  "cache_timeout": 0,
  "database_name": "gettingstarted",
  "expose_in_sqllab": true,
  "impersonate_user": true,
  "is_managed_externally": true,
  "sqlalchemy_uri": "solr+http://172.18.0.111:8983/solr/gettingstarted"
}' 


curl -X 'POST' \
  'http://172.18.0.140:8088/api/v1/database/' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $access_token" \
  -d '{
  "database": 1,
  "owners": [
    1
  ],
  "table_name": "gettingstarted"
}'