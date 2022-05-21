curl 'http://172.18.0.111:8983/solr/admin/collections?action=CREATE&name=gettingstarted&numShards=1&collection.configName=_default'

curl http://172.18.0.111:8983/solr/gettingstarted/config -d '{"set-user-property": {"update.autoCreateFields":"false"}}'