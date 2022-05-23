docker exec -it cassandra bash -c "cqlsh -e \"DROP TABLE IF EXISTS tsks.tstable;\""

docker exec -it cassandra bash -c "cqlsh -e \"DROP KEYSPACE IF EXISTS tsks;\""

docker exec -it cassandra bash -c "cqlsh -e \"CREATE KEYSPACE IF NOT EXISTS tsks
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};\""

docker exec -it cassandra bash -c "cqlsh -e \"CREATE TABLE IF NOT EXISTS tsks.tstable (
    name text,
    timestamp timestamp,
    property text,
    unit text,
    value double,
    PRIMARY KEY (name, timestamp)
    );\""

