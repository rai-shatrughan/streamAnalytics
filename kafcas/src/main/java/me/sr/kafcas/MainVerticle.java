package me.sr.kafcas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraClient;

import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.session.SessionBuilder;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;

import java.util.List;
import java.util.stream.Collector;

public class MainVerticle extends AbstractVerticle {

  Collector<Row, ?, String> listCollector;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    CqlSessionBuilder builder = CqlSession.builder();
    builder.withLocalDatacenter("datacenter1");

    CassandraClientOptions options = new CassandraClientOptions(builder)
    .addContactPoint("172.18.0.61", 9042);

    CassandraClient cassandraClient = CassandraClient.create(vertx, options);


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
}
