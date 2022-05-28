curl -X POST -H 'Content-type:application/json' --data-binary '{
  "replace-field":{
     "name":"tag",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "replace-field":{
     "name":"message",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "replace-field":{
     "name":"log.logger",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "replace-field":{
     "name":"log.level",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "replace-field":{
     "name":"process.thread.name",
	 "type":"text_general",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },	 
  "replace-field":{
     "name":"time",
	 "type":"pdates",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" },
  "replace-field":{
     "name":"_timestamp",
	 "type":"pdates",
     "multiValued":"false",
	 "indexed":"true",
	 "stored":"true" }	 
}' http://172.18.0.111:8983/solr/gettingstarted/schema