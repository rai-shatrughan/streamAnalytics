package sr.me.ws;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public class KafkaCassandraVerticle extends AbstractVerticle {

	private static final Logger logger = LogManager.getLogger();
	static KafkaConsumer<String, String> consumer;
	static CassandraClient cassandraClient;
	Collector<Row, ?, String> listCollector;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		initKafka(vertx);
		initCassandra(vertx);
		consumeWithPolling();
	}

	public void initKafka(Vertx vertx) {
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

	public void initCassandra(Vertx vertx) {
		CqlSessionBuilder builder = CqlSession.builder();
		builder.withLocalDatacenter(Constants.CASSANDRA_DATACENTER);

		CassandraClientOptions options = new CassandraClientOptions(builder).setTracingPolicy(TracingPolicy.ALWAYS)
				.addContactPoint(Constants.CASSANDRA_CONTACT_IP, Constants.CASSANDRA_CONTACT_PORT)
				.setKeyspace(Constants.CASSANDRA_KEYSPACE_TS);

		cassandraClient = CassandraClient.create(vertx, options);
	}

	public void executeStatement(SimpleStatement simpleStatement) {
		cassandraClient.execute(simpleStatement, result -> {
			if (result.succeeded()) {
				logger.info("Query executed successfully");
			} else {
				logger.error("Unable to execute Query");
				result.cause().printStackTrace();
			}
		});
	}

	public void executeBatchStatement(BatchStatement batchStatement) {
		cassandraClient.execute(batchStatement, result -> {
			if (result.succeeded()) {
				logger.info("Batch Query SUCCESS");
			} else {
				logger.error("Batch Query FAILED");
				result.cause().printStackTrace();
			}
		});
	}

	public void consumeSimple() {
		consumer.subscribe(Constants.KAFKA_TS_TOPIC);
		consumer.handler(record -> {
			logger.info("Processing partition=" + record.partition() + " ,offset=" + record.offset());
			logger.debug("key=" + record.key() + ",value=" + record.value() + ",partition=" + record.partition()
					+ ",offset=" + record.offset());
			SimpleStatement simpleStatement = SimpleStatement
					.newInstance("INSERT INTO " + Constants.CASSANDRA_TABLE_TS + " JSON '" + record.value() + "'");
			executeStatement(simpleStatement);
		});
	}

	public void consumeWithPolling() {

		consumer.subscribe(Constants.KAFKA_TS_TOPIC).onSuccess(v -> {
			logger.info("Consumer subscribed");

			// Let's poll every second
			vertx.setPeriodic(500, timerId -> consumer.poll(Duration.ofMillis(100)).onSuccess(records -> {
				for (int i = 0; i < records.size(); i++) {
					KafkaConsumerRecord<String, String> record = records.recordAt(i);
					logger.info("Processing partition=" + record.partition() + " ,offset=" + record.offset());
					logger.debug("key=" + record.key() + ",value=" + record.value() + ",partition=" + record.partition()
							+ ",offset=" + record.offset());
					SimpleStatement simpleStatement = SimpleStatement.newInstance(
							"INSERT INTO " + Constants.CASSANDRA_TABLE_TS + " JSON '" + record.value() + "'");
					executeStatement(simpleStatement);
				}
			}).onFailure(cause -> {
				logger.error("Something went wrong when polling " + cause.toString());
				cause.printStackTrace();

				// Stop polling if something went wrong
				vertx.cancelTimer(timerId);
			}));
		});
	}

}
