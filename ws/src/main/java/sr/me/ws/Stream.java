package sr.me.ws;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.datastax.spark.connector.japi.CassandraJavaUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.spark.sql.*;

// import org.apache.hadoop.conf.Configuration;
// import org.apache.hadoop.hbase.HBaseConfiguration;
// import org.apache.hadoop.hbase.HColumnDescriptor;
// import org.apache.hadoop.hbase.HTableDescriptor;
// import org.apache.hadoop.hbase.MasterNotRunningException;
// import org.apache.hadoop.hbase.TableName;
// import org.apache.hadoop.hbase.ZooKeeperConnectionException;
// import org.apache.hadoop.hbase.client.Admin;
// import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
// import org.apache.hadoop.hbase.client.Connection;
// import org.apache.hadoop.hbase.client.ConnectionFactory;
// import org.apache.hadoop.hbase.client.HBaseAdmin;
// import org.apache.hadoop.hbase.client.HTable;
// import org.apache.hadoop.hbase.client.Put;
// import org.apache.hadoop.hbase.client.Table;
// import org.apache.hadoop.hbase.client.TableDescriptor;
// import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
// import org.apache.hadoop.hbase.util.Bytes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scala.Tuple2;
import scala.Tuple5;
import scala.tools.nsc.doc.html.HtmlTags.U;

public class Stream implements Serializable {

  class TimeSeries {
    String name;
    String timestamp;
    String property;
    String unit;
    Double value;
  }

  private static final Logger logger = LogManager.getLogger();
  static Map<String, Object> kafkaParams = new HashMap<>();

  public static void main(String[] args) throws InterruptedException, IOException {

    System.setProperty("hadoop.home.dir", "./sparkle");

    // Create a local StreamingContext with two working thread and batch
    // interval of 1 second
    SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Sampleapp").set("spark.executor.cores", "4")
        .set("spark.driver.memory", "8g").set("spark.executor.memory", "8g")
        .set("spark.streaming.kafka.maxRatePerPartition", "100000")
        .set("spark.cassandra.connection.host", Constants.CASSANDRA_CONTACT_IP);

    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(10)); // tune
                                                                                     // this
                                                                                     // for
                                                                                     // performance

    SQLContext sqlContext = new SQLContext(sc);

    kafkaParams.put("bootstrap.servers", Constants.KAFKA_BOOTSTRAP_SERVERS);
    kafkaParams.put("key.deserializer", StringDeserializer.class);
    kafkaParams.put("value.deserializer", StringDeserializer.class);
    kafkaParams.put("group.id", "0");
    kafkaParams.put("auto.offset.reset", "earliest"); // from-beginning?
    // kafkaParams.put("auto.offset.reset", "latest"); // latest?
    kafkaParams.put("enable.auto.commit", false);

    Collection<String> topics = Arrays.asList(Constants.KAFKA_TS_TOPIC);

    final JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(jssc,
        LocationStrategies.PreferConsistent(),
        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

    // JavaPairDStream<String, String> jPairDStream = stream
    // .mapToPair(new PairFunction<ConsumerRecord<String, String>, String, String>()
    // {
    // @Override
    // public Tuple2<String, String> call(ConsumerRecord<String, String> record)
    // throws Exception {
    // return new Tuple2<>(record.key(), record.value());
    // }
    // });

    JavaDStream<String> javaDStream = stream
        .map(new Function<ConsumerRecord<String, String>, String>() {
          public String call(ConsumerRecord<String, String> record) {
            return record.value();
          }
        });

    javaDStream.foreachRDD(rdd -> {

      try {
        // CassandraJavaUtil.javaFunctions(rdd)
        // .writerBuilder(Constants.CASSANDRA_KEYSPACE_TS, Constants.CASSANDRA_TABLE_TS,
        // CassandraJavaUtil.mapToRow(TimeSeries.class))
        // .saveToCassandra();

        System.out.println(rdd.toString());

      } catch (NullPointerException e) {
        System.out.println("Error getting TimeSeries Data from Payload : " + rdd);
        return;
      } catch (Exception e) {
        System.out.println("Error writing TimeSeries Data to Hbase for RDD : " + rdd);
        return;
      }

      System.out.println("**************************");
    });

    jssc.start();// Start the computation
    jssc.awaitTermination();

  }

}
