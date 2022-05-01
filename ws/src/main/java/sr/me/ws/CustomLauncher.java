package sr.me.ws;

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


public class CustomLauncher extends Launcher {

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
      .setEnabled(true)
      .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true));

    options.setMetricsOptions(metricsOptions);
    options.setTracingOptions(new OpenTracingOptions(getTracer("sr-me-ws-v1")));
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
    registry.config().meterFilter(
      new MeterFilter() {
        @Override
        public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
          return DistributionStatisticConfig.builder()
            .percentilesHistogram(true)
            .build()
            .merge(config);
        }
    });
  }

  private static JaegerTracer getTracer(String name){
    SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv()
      .withType(ConstSampler.TYPE)
      .withParam(1);

    ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv()
      .withLogSpans(true);

    Configuration config = new Configuration(name)
      .withSampler(samplerConfig)
      .withReporter(reporterConfig);

    JaegerTracer tracer = config.getTracer();

    return tracer;
  }

  public static void main(String[] args) {
    // IMPORTANT
    // This is required to use our custom launcher.
    new CustomLauncher().dispatch(args);
  }

}
