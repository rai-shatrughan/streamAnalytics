package sr.me.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;

import sr.me.common.Constants;
import sr.me.handler.APIHandler;

public class WSVerticle extends AbstractVerticle {

  int port = Constants.WS_SERVER_PORT;
  String defaultPath = "/";
  String basePath = "/api/v1/";
  String timeSeriesPath = "ts";
  String streamPath = "stream";
  String metricsPath = "metrics";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    APIHandler apiHandler = new APIHandler(vertx);

    // serve static files from src/main/resources/webroot
    router.get().handler(StaticHandler.create());

    Route defaultRoute = router.route(defaultPath);
    Route timeSeriesRoute = router.route(basePath + timeSeriesPath);
    Route streamRoute = router.route(basePath + streamPath);
    Route metricsRoute = router.route(defaultPath + metricsPath);

    defaultRoute.handler(ctx -> apiHandler.defaultRouteHandler(ctx));
    timeSeriesRoute.handler(ctx -> apiHandler.timeSeriesHandler(ctx));
    streamRoute.handler(ctx -> apiHandler.streamHandler(ctx));
    metricsRoute.handler(PrometheusScrapingHandler.create());

    server.requestHandler(router).listen(port, res -> apiHandler.serverHandler(port, res));

  }
}
