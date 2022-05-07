package sr.me.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.Route;
import io.vertx.core.http.HttpServer;
import io.vertx.micrometer.PrometheusScrapingHandler;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.Launcher;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.tracing.opentracing.OpenTracingOptions;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.samplers.ConstSampler;

public class MainVerticle extends AbstractVerticle {

  int port = Constants.WS_SERVER_PORT;
  String jaegerServiceName = "sr-me-ws-v1.0";
  String defaultPath = "/";
  String basePath = "/api/v1/";
  String timeSeriesPath = "ts";
  String streamPath = "stream";
  String metricsPath = "metrics";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
    .setEnabled(true)
    .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true));

    SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv()
    .withType(ConstSampler.TYPE)
    .withParam(0);

    ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv()
      .withLogSpans(false);

    Configuration config = new Configuration(jaegerServiceName)
      .withSampler(samplerConfig)
      .withReporter(reporterConfig);

    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setMetricsOptions(metricsOptions)
      .setTracingOptions(new OpenTracingOptions(config.getTracer())));

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    Handlers handlers = new Handlers(vertx);

    Route defaultRoute = router.route(defaultPath);
    Route timeSeriesRoute = router.route(basePath+timeSeriesPath);
    Route streamRoute = router.route(basePath+streamPath);
    Route metricsRoute = router.route(defaultPath+metricsPath);

    defaultRoute.handler(ctx -> handlers.defaultRouteHandler(ctx));
    timeSeriesRoute.handler(ctx -> handlers.timeSeriesHandler(ctx));
    streamRoute.handler(ctx -> handlers.streamHandler(ctx));
    metricsRoute.handler(PrometheusScrapingHandler.create());

    server.requestHandler(router)
          .listen(port, res -> handlers.serverHandler(port, res));

  }
}
