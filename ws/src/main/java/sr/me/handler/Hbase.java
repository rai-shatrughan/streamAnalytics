package sr.me.handler;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import sr.me.common.Constants;

public class Hbase {

  static HBaseConfiguration hBaseConfiguration;
  static Configuration conf;
  static Admin admin;
  public static Connection connection;

  public static void getHbaseConnection() throws IOException {

    conf = HBaseConfiguration.create();
    conf.clear();
    conf.set("hbase.zookeeper.quorum", Constants.HBASE_ZK_QUORUM);
    connection = ConnectionFactory.createConnection(conf);
  }

  public static void createHbaseTable(String tableName) throws IOException {
    TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
        .setColumnFamily(ColumnFamilyDescriptorBuilder.of("fam1")).build();
    admin.createTable(tableDescriptor);

  }

  public static void putHbaseTable(String tableName, String key, String timeSeriesValues) throws IOException {

    // get the table
    Table table = connection.getTable(TableName.valueOf(tableName));
    try {

      // put a row
      byte[] prow = Bytes.toBytes(key);
      Put p = new Put(prow);
      p.addColumn("timeStamp".getBytes(), key.getBytes(), timeSeriesValues.getBytes());
      // p.addColumn("aspect".getBytes(), key.getBytes(),
      // timeSeriesValues.getBytes());
      table.put(p);
      System.out.println("+++Success+++");
      // table.close();

    } finally {
      try {
        // close table connection
        if (table != null) {
          table.close();
        } else {
          System.out.println("No such table");
        }
        // close hbase connection
        // if (connection != null && !connection.isClosed()) {
        // connection.close();
        // }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

  }

  public static void getHbaseTable(String tableName, String key) throws IOException {

    // get the table
    Table table = connection.getTable(TableName.valueOf(tableName));
    try {

      byte[] row = Bytes.toBytes(key);
      Get g = new Get(row);
      Result getResult = table.get(g);
      String v = Bytes.toString(getResult.getValue("timeStamp".getBytes(), key.getBytes()));
      System.out.println("Column key value:: " + v);

    } catch (Exception e2) {
      e2.printStackTrace();
    } finally {
      table.close();
    }
  }

  // public static void main(String[] args) throws IOException, ParseException{
  // getHbaseConnection();
  //
  //
  // Instant instantStart = Instant.parse("2019-06-12T09:01:56.000Z");
  // Instant instantEnd = Instant.parse("2019-06-12T09:01:59.000Z");
  // Instant currentInstant = instantStart;
  // Duration gap = Duration.ofSeconds(1);
  //
  // ArrayList<Instant> instantArrayList = new ArrayList<Instant>();
  //
  // System.out.println(instantStart.toString().replace("Z", ".000Z"));
  // System.out.println(instantEnd.toString().replace("Z", ".000Z"));
  //
  // while( instantEnd.compareTo(currentInstant)>=0 ){
  // String key =
  // "0669019257684578b536eded233c7639_"+currentInstant.toString().replace("Z",
  // ".000Z");
  // getHbaseTable("myTable",key);
  // System.out.println(key);
  // currentInstant = currentInstant.plusSeconds(1);
  //
  // }
  //

  // }

}
