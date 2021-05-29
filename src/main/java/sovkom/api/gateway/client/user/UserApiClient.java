package sovkom.api.gateway.client.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.hilariousstartups.soap.gen.GetUserRequest;
import ru.hilariousstartups.soap.gen.GetUserResponse;
import ru.hilariousstartups.soap.gen.SoapServicePort;
import sovkom.api.configuration.CxfClientConfiguration;

@Component
public class UserApiClient {

    private final SoapServicePort client;

    public UserApiClient(@Value("${users-api.uri}") String uri,
                         CxfClientConfiguration.CxfClientFactory clientFactory
    ) {
        client = clientFactory.create(SoapServicePort.class, uri);
    }

    public Mono<GetUserResponse> getUser(GetUserRequest request) {
        return Mono.create(sink ->
            client.getUserAsync(request, outputFuture -> {
                try {
                    sink.success(outputFuture.get());
                } catch (Exception e) {
                    sink.error(e);
                }
            })
        );
    }
}
