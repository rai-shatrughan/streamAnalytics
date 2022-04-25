package sr.me.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.Route;
import io.vertx.core.http.HttpServer;

public class MainVerticle extends AbstractVerticle {

  int port = Constants.WS_SERVER_PORT;
  String defaultPath = "/";
  String basePath = "/api/v1/";
  String timeSeriesPath = "ts";
  String streamPath = "stream";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    Handlers handlers = new Handlers(vertx);

    Route defaultRoute = router.route(defaultPath);
    Route timeSeriesRoute = router.route(basePath+timeSeriesPath);
    Route streamRoute = router.route(basePath+streamPath);

    defaultRoute.handler(ctx -> handlers.defaultRouteHandler(ctx));
    timeSeriesRoute.handler(ctx -> handlers.timeSeriesHandler(ctx));
    streamRoute.handler(ctx -> handlers.streamHandler(ctx));

    server.requestHandler(router)
          .listen(port, res -> handlers.serverHandler(port, res));

  }
}
