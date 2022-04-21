package sr.me.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Route;
import io.vertx.core.http.HttpServer;

public class MainVerticle extends AbstractVerticle {

  int port = 8000;
  String defaultPath = "/";
  String basePath = "/api/v1/";
  String timeSeriesPath = "ts";
  String streamPath = "stream";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    Route defaultRoute = router.route(defaultPath);
    Route timeSeriesRoute = router.route(basePath+timeSeriesPath);
    Route streamRoute = router.route(basePath+streamPath);

    defaultRoute.handler(ctx -> Handlers.defaultRouteHandler(ctx));
    timeSeriesRoute.handler(ctx -> Handlers.timeSeriesHandler(ctx));
    streamRoute.handler(ctx -> Handlers.streamHandler(ctx));

    server.requestHandler(router)
          .listen(this.port, res -> Handlers.serverHandler(port, res));

  }
}
