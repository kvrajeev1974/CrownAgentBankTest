package cab.external.client;

import cab.config.SegoviaClientProperties;
import cab.entity.PaymentRequest;
import cab.entity.PaymentResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class SegoviaWebClient {

    private final WebClient webclient;

    private final SegoviaClientProperties properties;

    public SegoviaWebClient(WebClient webclient, SegoviaClientProperties properties) {
        this.webclient = webclient;
        this.properties = properties;
    }

    @SneakyThrows
    public PaymentResponse makePayment(PaymentRequest request) {
        log.info("About to call the payment provider by thread - {} {}", Thread.currentThread(), request);
        return webclient
                .post()
                .uri(properties.url())
                .headers(headers -> headers.setBearerAuth(getToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .timeout(Duration.of(properties.timeoutMillis(), ChronoUnit.MILLIS))
                .toFuture()
                .get();
    }

    @SneakyThrows
    private String getToken() {
        return webclient
                .post()
                .uri(properties.authUrl())
                .headers(headers -> {
                    headers.set("Content-Type", "application/json");
                    headers.set("Api-Key", properties.apiKey());
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"account\": \"" + properties.account() + "\"}")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(tokenResponse ->
                        tokenResponse.get("token").textValue()
                )
                .toFuture()
                .get();
    }


}
