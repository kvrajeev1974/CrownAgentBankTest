package cab.config;

import cab.entity.PaymentRequest;
import cab.exception.SegoviaClientException;
import com.google.common.collect.Lists;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@Slf4j
public class SegoviaConfig {

    @Bean
    public HeaderColumnNameTranslateMappingStrategy strategy() {
        HeaderColumnNameTranslateMappingStrategy strategy = new HeaderColumnNameTranslateMappingStrategy();
        strategy.setType(PaymentRequest.class);
        return strategy;
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        return filter;
    }

    @Bean
    public WebClient webClient() {
        WebClient.Builder builder = WebClient.builder();
        return builder.filters(list -> {
            list.add(logRequest());
            list.add(errorHandler());
        }).build();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            /*if (clientResponse.statusCode().is5xxServerError()) {
                log.debug("Rajeev 500 status = {}", clientResponse.statusCode());
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new SegoviaClientException(errorBody)));
            } else if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new SegoviaClientException(errorBody)));
            } else {*/
                log.debug("Rajeev status = {}", clientResponse.statusCode());
                return Mono.just(clientResponse);
            //}
        });
    }

}
