package sr.me.common;

public class Constants {

  public static final String CASSANDRA_DATACENTER = System.getenv().getOrDefault("CASSANDRA_DATACENTER",
      "datacenter1");
  public static final String CASSANDRA_CONTACT_IP = System.getenv().getOrDefault("CASSANDRA_CONTACT_IP",
      "172.18.0.61");
  public static final int CASSANDRA_CONTACT_PORT = Integer
      .parseInt(System.getenv().getOrDefault("CASSANDRA_CONTACT_PORT", "9042"));
  public static final String CASSANDRA_REPLICATION_STRATEGY = System.getenv()
      .getOrDefault("CASSANDRA_REPLICATION_STRATEGY", "SimpleStrategy");
  public static final int CASSANDRA_REPLICATION_FACTOR = Integer
      .parseInt(System.getenv().getOrDefault("CASSANDRA_REPLICATION_FACTOR", "1"));
  public static final String CASSANDRA_KEYSPACE_TS = System.getenv().getOrDefault("CASSANDRA_KEYSPACE_TS", "tsks");
  public static final String CASSANDRA_TABLE_TS = System.getenv().getOrDefault("CASSANDRA_TABLE_TS", "tstable");

  // Kafka zookeeper is shared
  public static final String HBASE_ZK_QUORUM = System.getenv().getOrDefault("HBASE_ZK_QUORUM", "172.18.0.51");

  public static final String KAFKA_BOOTSTRAP_SERVERS = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS",
      "172.18.0.41:9092");
  public static final String KAFKA_TS_TOPIC = System.getenv().getOrDefault("KAFKA_TS_TOPIC", "ts");
  public static final String KAFKA_STREAM_TOPIC = System.getenv().getOrDefault("KAFKA_STREAM_TOPIC", "stream");

  public static final String WS_SERVER_IP = System.getenv().getOrDefault("WS_SERVER_IP", "172.18.0.21");
  public static final int WS_SERVER_PORT = Integer.parseInt(System.getenv().getOrDefault("WS_SERVER_PORT", "8000"));

  public static final String POD_NAME = System.getenv().getOrDefault("POD_NAME", "unknown");
}
