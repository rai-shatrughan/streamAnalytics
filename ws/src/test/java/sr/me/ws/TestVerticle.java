package sr.me.ws;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
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
		int workers = 1;

		DeploymentOptions depOptions = new DeploymentOptions();
		depOptions.setWorker(true).setWorkerPoolSize(workers);
		depOptions.setInstances(6000);

		VertxOptions vertxOptions = new VertxOptions();
		// default event pool size is (no. of cores*2)
		 vertxOptions.setEventLoopPoolSize(1);

		Vertx vertx = Vertx.vertx(vertxOptions);
		vertx.deployVerticle(TestVerticle::new, depOptions);
	}

	@Override
	public void start(Promise<Void> startPromise) {
		request = WebClient.create(vertx).post(Constants.WS_SERVER_PORT, Constants.WS_SERVER_IP, "/api/v1/ts")
				.ssl(false).putHeader("Accept", "application/json").as(BodyCodec.jsonObject())
				.expect(ResponsePredicate.SC_OK);

		vertx.setPeriodic(1000, id -> sendTS());
		startPromise.complete();
	}

	private void sendTS() {
		request.sendJsonObject(buildTSJson()).onSuccess(res -> {
			logger.info("Sent request:" + res.statusCode() + ":" + res.statusMessage());
		}).onFailure(err -> {
			logger.error("Failed request:" + err.getCause());
		});
	}

	private JsonObject buildTSJson() {
		JsonObject json = new JsonObject()
    .put("name","sr_plant_furnace1")
    .put("timestamp", Instant.now())
    .put("property", "temperature")
		.put("unit", "celcius")
    .put("value", generateTemp());

		return json;
	}

  private double generateTemp(){
    int min = 10;
    int max = 80;
    double a = Math.random()*(max-min+1)+min;
    return a;
  }

}
