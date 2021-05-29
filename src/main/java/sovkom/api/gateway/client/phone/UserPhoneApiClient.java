package sovkom.api.gateway.client.phone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sovkom.api.gateway.client.phone.dto.UserPhones;

@Component
public class UserPhoneApiClient {

    private final WebClient webClient;

    public UserPhoneApiClient(@Value("${phones-api.uri}") String uri,
                              WebClient.Builder webClientBuilder
    ) {
        webClient = webClientBuilder.baseUrl(uri)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Mono<UserPhones> getUserPhones(int userId) {
        return webClient.get()
            .uri(uri -> uri.path("/api/v1/phones/{userId}").build(userId))
            .retrieve()
            .bodyToMono(UserPhones.class);
    }
}
