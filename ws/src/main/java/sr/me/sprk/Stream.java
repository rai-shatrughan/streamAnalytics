package sr.me.sprk;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sr.me.common.Constants;
import sr.me.handler.HbaseHandler;

import scala.Tuple2;

public class Stream implements Serializable {
  private static final Logger logger = LogManager.getLogger();

  static String TABLE_NAME = "myTable";
  static String COLUMN_FAMILY = "cf1";
  static Map<String, Object> kafkaParams = new HashMap<>();

  public static String getTimeSeries(String rdd) {

    String multipartBoundary = StringUtils.substringBetween(rdd, "Content-Type: multipart/related;boundary=", "--")
        .trim();
    String timeSeries = StringUtils
        .substringBetween(rdd, "Content-Type: application/json", "--" + multipartBoundary + "--").trim();
    // System.out.println(rdd);
    // System.out.println(timeSeries);
    return timeSeries;
  }

  public static void main(String[] args) throws InterruptedException, IOException {

    System.setProperty("hadoop.home.dir", "./sparkle");

    HbaseHandler hb = new HbaseHandler();
    hb.createHbaseTable(TABLE_NAME, COLUMN_FAMILY);

    // Create a local StreamingContext with two working thread and batch
    // interval of 1 second
    SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Sampleapp").set("spark.executor.cores", "4")
        .set("spark.driver.memory", "8g").set("spark.executor.memory", "8g")
        .set("spark.streaming.kafka.maxRatePerPartition", "100000");

    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(5));

    SQLContext sqlContext = new SQLContext(sc);

    kafkaParams.put("bootstrap.servers", Constants.KAFKA_BOOTSTRAP_SERVERS);
    kafkaParams.put("key.deserializer", StringDeserializer.class);
    kafkaParams.put("value.deserializer", StringDeserializer.class);
    kafkaParams.put("group.id", "0");
    // kafkaParams.put("auto.offset.reset", "earliest"); // from-beginning?
    kafkaParams.put("auto.offset.reset", "latest"); // latest?
    kafkaParams.put("enable.auto.commit", false);

    Collection<String> topics = Arrays.asList(Constants.KAFKA_TS_TOPIC);

    final JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(jssc,
        LocationStrategies.PreferConsistent(),
        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

    JavaPairDStream<String, String> jPairDStream = stream
        .mapToPair(new PairFunction<ConsumerRecord<String, String>, String, String>() {
          @Override
          public Tuple2<String, String> call(ConsumerRecord<String, String> record) throws Exception {
            return new Tuple2<>(record.key(), record.value());
          }
        });

    jPairDStream.foreachRDD(jPairRDD -> {
      jPairRDD.foreach(rdd -> {
      List<Put> putList = new ArrayList<Put>();
      Table table = hb.getTable(TABLE_NAME);
        JsonObject jsonObject = new JsonObject(rdd._2());
        try {
          String key = jsonObject.getString("name") + "_"
              + jsonObject.getString("timestamp").replace(":", "-").replace(".", "-");
          byte[] prow = Bytes.toBytes(key);
          Put p = new Put(prow);
          p.addColumn(COLUMN_FAMILY.getBytes(), key.getBytes(), rdd._2().getBytes());
          putList.add(p);
          logger.info(key);
        } catch (Exception e) {
          System.out.println("Error writing TimeSeries Data to Hbase for RDD : " + rdd._2());
          return;
        }
      hb.puts(table, putList);
      table.close();
      });
    });

    jssc.start();// Start the computation
    jssc.awaitTermination();

  }

}
