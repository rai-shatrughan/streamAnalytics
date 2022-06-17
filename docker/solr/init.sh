curl 'http://172.18.0.111:8983/solr/admin/collections?action=CREATE&name=gettingstarted&numShards=1&collection.configName=_default'

#curl http://172.18.0.111:8983/solr/gettingstarted/config -d '{"set-user-property": {"update.autoCreateFields":"false"}}'

curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{
     "name":"log.level",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"message",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"log.logger",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"process.thread.name",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"tag",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },	 
  "add-field":{
     "name":"time",
	 "type":"pdates",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"_timestamp",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "add-field":{
     "name":"level",
     "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
     "stored":"true" },
  "add-field":{
     "name":"logger",
     "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
     "stored":"true" },
  "add-field":{
     "name":"thread_name",
     "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
     "stored":"true" },	 
}' http://172.18.0.111:8983/solr/gettingstarted/schema


curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-copy-field":{
     "source":"log.level",
     "dest":"level" },
  "add-copy-field":{
     "source":"log.logger",
     "dest":"logger"},
  "add-copy-field":{
     "source":"process.thread.name",
     "dest":"thread_name"}
}' http://172.18.0.111:8983/solr/gettingstarted/schema