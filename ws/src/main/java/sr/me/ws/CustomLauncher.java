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

public class CustomLauncher extends Launcher {

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
      .setEnabled(true)
      .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true));

    options.setMetricsOptions(metricsOptions);
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

  public static void main(String[] args) {
    // IMPORTANT
    // This is required to use our custom launcher.
    new CustomLauncher().dispatch(args);
  }

}
