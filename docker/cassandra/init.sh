docker exec -it cassandra bash -c "cqlsh -e \"CREATE KEYSPACE IF NOT EXISTS tsks
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};\""

docker exec -it cassandra bash -c "cqlsh -e \"CREATE TABLE IF NOT EXISTS tsks.timeseries (
    name text PRIMARY KEY,
    timestamp timestamp,
    property text,
    unit text,
    value double
    );\""

docker exec -it cassandra bash -c "cqlsh -e \"SELECT * from tsks.timeseries;\""