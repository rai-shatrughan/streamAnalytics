package sr.me.ws;

import java.time.Instant;
import java.util.TimerTask;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

import sr.me.common.Constants;

public class TSTask extends TimerTask {
  private Vertx vertx;
  private HttpRequest<JsonObject> request;

  public TSTask(Vertx vertx) {
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
          System.out.print(Instant.now() + " : Resp Code - " + res.statusCode() + " : " + "Resp Message - "
              + res.statusMessage() + "\n");
        })
        .onFailure(err -> {
          System.out.print(Instant.now() + " : Failed request" + err.getCause() + "\n");
        });
  }

  private JsonObject buildTSJson() {
    JsonObject json = new JsonObject()
        .put("timestamp", Instant.now())
        .put("property", "temperature")
        .put("unit", "celcius")
        .put("value", 100.01);

    return json;
  }
}
