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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sr.me.common.Constants;

public class HbaseHandler {

  static HBaseConfiguration hBaseConfiguration;
  static Configuration conf;
  static Admin admin;
  public static Connection connection;
  static String TABLE_NAME = "myTable";
  static String COLUMN_FAMILY = "cf1";
  private static final Logger logger = LogManager.getLogger();

  public HbaseHandler() {
    conf = HBaseConfiguration.create();
    conf.set("hbase.zookeeper.quorum", Constants.HBASE_ZK_QUORUM);
    conf.setInt("hbase.zookeeper.property.clientPort", 2181);
    conf.setInt("hbase.client.retries.number", 0);
    conf.setInt("zookeeper.recovery.retry", 0);
    conf.set("hbase.rpc.timeout", "10000");
    conf.set("hbase.client.scanner.timeout.period", "10000");
    conf.set("hbase.cells.scanned.per.heartbeat.check", "10000");
    conf.set("zookeeper.session.timeout", "10000");
    try {
      connection = ConnectionFactory.createConnection(conf);
    } catch (Exception e) {
      logger.error(e.getCause());
    }
  }

  public void createHbaseTable(String tableName, String columnFamily) throws IOException {
    TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
        .setColumnFamily(ColumnFamilyDescriptorBuilder.of(columnFamily)).build();
    admin = connection.getAdmin();
    if (!admin.tableExists(tableDescriptor.getTableName())) {
      System.out.println("Creating Table : " + tableDescriptor.getTableName());
      admin.createTable(tableDescriptor);
    } else {
      System.out.println("Table already Exists, Skiping Table creation : " + tableDescriptor.getTableName());
    }
  }

  public void cleanTable(String tableName, String columnFamily) throws IOException {
    TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
        .setColumnFamily(ColumnFamilyDescriptorBuilder.of(columnFamily)).build();
    admin = connection.getAdmin();
    if (admin.tableExists(tableDescriptor.getTableName())) {
      System.out.println("Deleting Table : " + tableDescriptor.getTableName());
      admin.disableTable(tableDescriptor.getTableName());
      admin.deleteTable(tableDescriptor.getTableName());
    } else {
      System.out.println("Table Does not Exists, Skiping Table Deletion : " + tableDescriptor.getTableName());
    }
  }

  public void putHbaseTable(String tableName, String columnFamily, String key, String timeSeriesValues)
      throws IOException {

    // get the table
    Table table = connection.getTable(TableName.valueOf(tableName));
    try {

      // put a row
      byte[] prow = Bytes.toBytes(key);
      Put p = new Put(prow);
      p.addColumn(columnFamily.getBytes(), key.getBytes(), timeSeriesValues.getBytes());
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

  public void getHbaseTable(String tableName, String columnFamily, String key) throws IOException {

    // get the table
    Table table = connection.getTable(TableName.valueOf(tableName));
    try {

      byte[] row = Bytes.toBytes(key);
      Get g = new Get(row);
      Result getResult = table.get(g);
      String v = Bytes.toString(getResult.getValue(columnFamily.getBytes(), key.getBytes()));
      System.out.println("Column key value :: " + v);

    } catch (Exception e2) {
      e2.printStackTrace();
    } finally {
      table.close();
    }
  }

  // public static void main(String[] args) throws Exception {
  // HbaseHandler hb = new HbaseHandler();
  // // hb.cleanTable(TABLE_NAME, COLUMN_FAMILY);
  // // hb.createHbaseTable(TABLE_NAME, COLUMN_FAMILY);

  // Instant instantStart = Instant.parse("2019-06-12T09:01:56.000Z");
  // Instant instantEnd = Instant.parse("2019-06-12T09:01:59.000Z");
  // Instant currentInstant = instantStart;

  // System.out.println(instantStart.toString().replace("Z", ".000Z"));
  // System.out.println(instantEnd.toString().replace("Z", ".000Z"));

  // while (instantEnd.compareTo(currentInstant) >= 0) {
  // String key = "0669019257684578b536eded233c7639_" +
  // currentInstant.toString().replace("Z", ".000Z");
  // hb.getHbaseTable(TABLE_NAME, COLUMN_FAMILY, key);
  // // hb.putHbaseTable(TABLE_NAME, COLUMN_FAMILY, key, key);
  // System.out.println(key);
  // currentInstant = currentInstant.plusSeconds(1);
  // }
  // }
}
