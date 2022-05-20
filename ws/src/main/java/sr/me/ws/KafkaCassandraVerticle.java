package sr.me.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraClient;

import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.session.SessionBuilder;
import com.datastax.oss.protocol.internal.response.result.SetKeyspace;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collector;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;

public class KafkaCassandraVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger();
  static KafkaConsumer<String, String> consumer;
  static CassandraClient cassandraClient;
  Collector<Row, ?, String> listCollector;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    initKafka(vertx);
    initCassandra(vertx);

    consumer.handler(record -> {
      logger.info("Processing key=" + record.key() + ",value=" + record.value() +
        ",partition=" + record.partition() + ",offset=" + record.offset());

      BatchStatement batchStatement = BatchStatement.newInstance(BatchType.LOGGED)
        .add(SimpleStatement.newInstance("INSERT INTO NAMES (name) VALUES ('Pavel')"))
        .add(SimpleStatement.newInstance("INSERT INTO NAMES (name) VALUES ('Thomas')"))
        .add(SimpleStatement.newInstance("INSERT INTO NAMES (name) VALUES ('Julien')"));

      cassandraClient.execute(batchStatement, result -> {
        if (result.succeeded()) {
          System.out.println("The given batch executed successfully");
        } else {
          System.out.println("Unable to execute the batch");
          result.cause().printStackTrace();
        }
      });
    });

    consumer.subscribe(Constants.KAFKA_TS_TOPIC);

  }

  public void initKafka(Vertx vertx){
    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", Constants.KAFKA_BOOTSTRAP_SERVERS);
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "ws_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");
    config.put("acks", "1");
    consumer = KafkaConsumer.create(vertx, config);
    logger.info("Initializing Kafka Consumer");
  }

  public void initCassandra(Vertx vertx){
    CqlSessionBuilder builder = CqlSession.builder();
    builder.withLocalDatacenter(Constants.CASSANDRA_DATACENTER);

    CassandraClientOptions options = new CassandraClientOptions(builder)
      .addContactPoint(Constants.CASSANDRA_CONTACT_IP, Constants.CASSANDRA_CONTACT_PORT)
      .setKeyspace(Constants.CASSANDRA_KEYSPACE_TS);

    cassandraClient = CassandraClient.create(vertx, options);
    createKeyspace();
    createTable();
  }

  public void getCassandraResult(){
    cassandraClient.executeWithFullFetch("SELECT userid, item_count FROM store.shopping_cart", executeWithFullFetch -> {
      if (executeWithFullFetch.succeeded()) {
        List<Row> rows = executeWithFullFetch.result();
        for (Row row : rows) {
          System.out.println(row.getObject(0) + ":" + row.getObject(1));
        }
      } else {
        System.out.println("Unable to execute the query");
        executeWithFullFetch.cause().printStackTrace();
      }
    });
  }

  public void createKeyspace(){
    StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
      .append(Constants.CASSANDRA_KEYSPACE_TS).append(" WITH replication = {")
      .append("'class':'").append(Constants.CASSANDRA_REPLICATION_STRATEGY)
      .append("','replication_factor':").append(Constants.CASSANDRA_REPLICATION_FACTOR)
      .append("};");

    String query = sb.toString();
    cassandraClient.execute(query, ar -> {
      if (ar.succeeded()) {
        System.out.println("Got " + ar.toString());
      } else {
        System.out.println("Failure Create KeySpace: " + ar.cause().getMessage());
      }
    });
  }

  public void createTable() {
    StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
      .append(Constants.CASSANDRA_TABLE_TS).append("(")
      .append("id uuid PRIMARY KEY, ")
      .append("title text,")
      .append("subject text);");

    String query = sb.toString();
    cassandraClient.execute(query, ar -> {
      if (ar.succeeded()) {
        System.out.println("Got " + ar.toString());
      } else {
        System.out.println("Failure Create Table: " + ar.cause().getMessage());
      }
    });
}

}
