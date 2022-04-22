package sr.me.ws;

public class Constants {

  public static final String KAFKA_BOOTSTRAP_SERVERS = System.getProperty("udv_kafkaBootstrapServers", "172.18.0.41:9092");
  public static final String KAFKA_TS_TOPIC = System.getProperty("udv_kafkaTSTopic", "ts");
  public static final String KAFKA_STREAM_TOPIC = System.getProperty("udv_kafkaStreamTopic", "stream");

  public static final int WS_SERVER_PORT = Integer.parseInt(System.getProperty("udv_wsServerPort", "8000"));

}
