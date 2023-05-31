package cab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("executor")
public record MultiThreadingProperties(int corePoolSize, int maxPoolSize, int maxQueueCapacity, String threadNamePrefix) {
}
