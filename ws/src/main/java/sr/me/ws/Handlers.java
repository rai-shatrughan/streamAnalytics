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

public class Handlers {

  static Vertx vertx = Vertx.vertx();

  public static void serverHandler(int port, AsyncResult<HttpServer> res) {
    if (res.succeeded()) {
      System.out.println("Server is now listening! - "+ port);
    } else {
      System.out.println("Failed to bind!");
    }
  }

  public static void defaultRouteHandler(RoutingContext ctx) {
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end("Hello World from Vert.x-Web!");
  }

  public static void timeSeriesHandler(RoutingContext ctx) {
    kafkaWriter(ctx);
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end("Hello TS!");
  }

  public static void streamHandler(RoutingContext ctx) {
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "text/plain");
    response.end("Hello Streams!");
  }

  private static void kafkaWriter(RoutingContext ctx){
      Map<String, String> config = new HashMap<>();
      config.put("bootstrap.servers", "172.18.0.41:9092");
      config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      config.put("acks", "1");

      System.out.println("Hello Kafka: " );

      // use producer for interacting with Apache Kafka
      KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);

      KafkaProducerRecord<String, String> record = KafkaProducerRecord.create("test1", "message_" + 0);
      producer.send(record)
        .onSuccess(recordMetadata ->
          System.out.println(
            "Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
            ", partition=" + recordMetadata.getPartition() +
            ", offset=" + recordMetadata.getOffset()
          )
          )
        .onFailure(cause -> System.out.println("Write failed: " + cause));

  }

}
