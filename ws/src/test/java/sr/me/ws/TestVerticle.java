package sr.me.ws;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

public class TestVerticle extends AbstractVerticle {
  private static final Logger logger = LogManager.getLogger();
  private HttpRequest<JsonObject> request;

  public static void main(String[] args) {
    int workers = 1000;
    int countVertx = 2000;
    boolean enableWorker = false;
    DeploymentOptions depOptions = new DeploymentOptions();
    depOptions.setWorker(true).setWorkerPoolSize(workers);

    Vertx vertx = Vertx.vertx();
    for(int i=0; i<=countVertx; i++){
      if (enableWorker) {
        vertx.deployVerticle(new TestVerticle(), depOptions);
      } else {
        vertx.deployVerticle(new TestVerticle());
      }
    }
  }

  @Override
  public void start() {
    request = WebClient.create(vertx)
      .post(Constants.WS_SERVER_PORT, Constants.WS_SERVER_IP, "/api/v1/ts")
      .ssl(false)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK);

    vertx.setPeriodic(1000, id -> sendTS());
    // sendTS();
  }

  private void sendTS() {
    request.sendJsonObject(buildTSJson())
      .onSuccess(res -> {
        logger.info("Sent request:" + res.statusCode() + ":" + res.statusMessage());
        })
      .onFailure(err -> {logger.error("Failed request:" + err.getCause());
        });
    }

  private JsonObject buildTSJson(){
    JsonObject json = new JsonObject()
      .put("timestamp", Instant.now())
      .put("property", "temperature")
      .put("unit", "celcius")
      .put("value", 100.01);

    return json;
  }

}
