package sr.me.swag;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import sr.me.ws.Handlers;

public class IoTVerticle extends AbstractVerticle {

  // int port = Constants.WS_SERVER_PORT;
  private HttpServer server;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Handlers handlers = new Handlers(vertx);

    RouterBuilder.create(this.vertx, "swagger/iot-ts.yaml")
        .onSuccess(routerBuilder -> { // (1)

          routerBuilder.operation("createOrUpdateTimeseries")
              .handler(routingContext -> handlers.timeSeriesHandler(routingContext));

          routerBuilder.operation("retrieveTimeseries")
              .handler(routingContext -> handlers.timeSeriesHandler(routingContext));

          Router router = routerBuilder.createRouter(); // (1)
          router.errorHandler(404, routingContext -> { // (2)
            JsonObject errorObject = new JsonObject() // (3)
                .put("code", 404)
                .put("message",
                    (routingContext.failure() != null) ? routingContext.failure().getMessage()
                        : "Not Found");
            routingContext
                .response()
                .setStatusCode(404)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(errorObject.encode()); // (4)
          });
          router.errorHandler(400, routingContext -> {
            JsonObject errorObject = new JsonObject()
                .put("code", 400)
                .put("message",
                    (routingContext.failure() != null) ? routingContext.failure().getMessage()
                        : "Validation Exception");
            routingContext
                .response()
                .setStatusCode(400)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(errorObject.encode());
          });

          // server = vertx.createHttpServer(new
          // HttpServerOptions().setPort(8080).setHost("localhost")); // (5)

          SelfSignedCertificate cert = SelfSignedCertificate.create();

          // Start an HTTP/2 server that prints hello world!
          vertx.createHttpServer(new HttpServerOptions()
              .setSsl(true)
              .setUseAlpn(true)
              .setKeyCertOptions(cert.keyCertOptions())
              .setPort(8443))
              .requestHandler(request -> {
                request.response().end("Hello World");
              }).listen();

          server.requestHandler(router).listen(); // (6)

        }).onFailure(cause -> { // (2)
          // Something went wrong during router factory initialization
          startPromise.fail(cause);
        });

  }
}
