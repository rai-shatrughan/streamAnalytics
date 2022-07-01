package sr.me.handler;

import java.io.IOException;
import java.util.List;
import java.time.Instant;
import java.io.Serializable;

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

public class HbaseHandler implements Serializable {

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

  public Table getTable(String tableName){
    Table table = null;
    try{
      table = connection.getTable(TableName.valueOf(tableName));
    } catch (Exception e){
      logger.error(e.getCause());
    }
    return table;
  }

  public void put(Table table, String columnFamily, String key, String timeSeriesValues) {
    try {
      byte[] prow = Bytes.toBytes(key);
      Put p = new Put(prow);
      p.addColumn(columnFamily.getBytes(), key.getBytes(), timeSeriesValues.getBytes());
      table.put(p);
      System.out.println("+++Success+++");

    } catch (Exception e2) {
        e2.printStackTrace();
    }

  }

  public void get(Table table, String columnFamily, String key) throws IOException {
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

  public void puts(Table table, List<Put> putList) {
   try{
    table.put(putList);
    logger.info("Success Putting");
   } catch (Exception e){
     logger.error(e.getCause());
   }
  }

  public static void main(String[] args) throws Exception {
  HbaseHandler hb = new HbaseHandler();
  hb.cleanTable(TABLE_NAME, COLUMN_FAMILY);
  hb.createHbaseTable(TABLE_NAME, COLUMN_FAMILY);
  Table table = hb.getTable(TABLE_NAME);

  Instant instantStart = Instant.parse("2019-06-12T09:01:56.000Z");
  Instant instantEnd = Instant.parse("2019-06-12T09:01:59.000Z");
  Instant currentInstant = instantStart;

  System.out.println(instantStart.toString().replace("Z", ".000Z"));
  System.out.println(instantEnd.toString().replace("Z", ".000Z"));

  while (instantEnd.compareTo(currentInstant) >= 0) {
  String key = "0669019257684578b536eded233c7639_" +
  currentInstant.toString().replace("Z", ".000Z");
  hb.get(table, COLUMN_FAMILY, key);
  hb.put(table, COLUMN_FAMILY, key, key);
  System.out.println(key);
  currentInstant = currentInstant.plusSeconds(1);
  }
  }
}
