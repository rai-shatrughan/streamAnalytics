package sr.me.ws;

public class Constants {

  public static final String KAFKA_BOOTSTRAP_SERVERS = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "172.18.0.41:9092");
  public static final String KAFKA_TS_TOPIC = System.getenv().getOrDefault("KAFKA_TS_TOPIC", "ts");
  public static final String KAFKA_STREAM_TOPIC = System.getenv().getOrDefault("KAFKA_STREAM_TOPIC", "stream");

  public static final int WS_SERVER_PORT = Integer.parseInt(System.getenv().getOrDefault("WS_SERVER_PORT", "8000"));

  public static final String POD_NAME = System.getenv().getOrDefault("POD_NAME", "unknown");
}
