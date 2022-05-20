package sr.me.ws;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handlers {

  private static final Logger logger = LogManager.getLogger();

  static KafkaProducer<String, String> producer;

  public Handlers(Vertx vertx){
    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", Constants.KAFKA_BOOTSTRAP_SERVERS);
    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("acks", "1");
    producer = KafkaProducer.create(vertx, config);
    System.out.println("Initializing Kafka Producer");
  }

  public void serverHandler(int port, AsyncResult<HttpServer> res) {
    if (res.succeeded()) {
      System.out.println("Server is now listening! - "+ port);
    } else {
      System.out.println("Failed to bind!");
    }
  }

  public void defaultRouteHandler(RoutingContext ctx) {
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end("Hello World from Vert.x-Web!");
  }

  public void timeSeriesHandler(RoutingContext ctx) {
    String tsJson = ctx.getBodyAsString();
    kafkaWriter(ctx, Constants.KAFKA_TS_TOPIC, tsJson);
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end(tsJson);
  }

  public void streamHandler(RoutingContext ctx) {
    kafkaWriter(ctx, Constants.KAFKA_STREAM_TOPIC, "message_stream");
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end("Hello Streams!");
  }

  private static void kafkaWriter(RoutingContext ctx, String topic, String message){
      // System.out.println("Hello Kafka: " );
      KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(topic, message);

      producer.send(record)
        .onSuccess(recordMetadata ->
          logger.info(
                "Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                ", partition=" + recordMetadata.getPartition() +
                ", offset=" + recordMetadata.getOffset()
              )
            )
        .onFailure(cause -> logger.error("Kafka Write failed: " + cause));

  }

}
