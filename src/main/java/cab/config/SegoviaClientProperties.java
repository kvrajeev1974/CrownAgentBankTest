package cab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("segovia")
public record SegoviaClientProperties(String url, String authUrl, long timeoutMillis, String apiKey, String account) {
}
