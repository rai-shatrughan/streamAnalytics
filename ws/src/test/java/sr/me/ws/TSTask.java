package sr.me.ws;

import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.core.VertxOptions;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.json.schema.common.dsl.Schemas;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;


public class TSTask extends TimerTask {
  private Vertx vertx;
  private HttpRequest<JsonObject> request;

  public TSTask(Vertx vertx){
    this.vertx = vertx;
  }

  @Override
	public void run() {
    this.start(vertx);
    this.sendTS();
  }

  private void start(Vertx vertx) {
    request = WebClient.create(vertx)
      .post(Constants.WS_SERVER_PORT, Constants.WS_SERVER_IP, "/api/v1/ts")
      .ssl(false)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK);
  }

  private void sendTS() {
    request.sendJsonObject(buildTSJson())
      .onSuccess(res -> {
        System.out.print(Instant.now() + " : Resp Code - " + res.statusCode() + " : "+ "Resp Message - " + res.statusMessage() + "\n");
        })
      .onFailure(err -> {System.out.print(Instant.now() + " : Failed request" + err.getCause() + "\n");
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