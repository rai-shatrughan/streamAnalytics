package sr.me.ws;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.json.schema.common.dsl.Schemas;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

public class TestVerticle extends AbstractVerticle {
  private HttpRequest<JsonObject> request;

  public static void main(String[] args) {
    int workers = 1000;
    int countVertx = 1;
    boolean enableWorker = true;

    // VertxOptions vertxOptions = new VertxOptions();
    // vertxOptions.setMaxEventLoopExecuteTime(10000);
    // vertxOptions.setBlockedThreadCheckInterval(10000);
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
    request = WebClient.create(vertx) // (1)
      .post(Constants.WS_SERVER_PORT, Constants.WS_SERVER_IP, "/api/v1/ts")
      .ssl(false)  // (3)
      .putHeader("Accept", "application/json")  // (4)
      .as(BodyCodec.jsonObject()) // (5)
      .expect(ResponsePredicate.SC_OK);  // (6)

    vertx.setPeriodic(1, id -> sendTS());
    // sendTS();
  }

  private void sendTS() {
    request.sendJsonObject(buildTSJson())
      .onSuccess(res -> {
        System.out.print("Sent request" + res.statusCode() + ":" + res.statusMessage());
        })
      .onFailure(err -> {System.out.print("Failed request" + err.getCause());
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
