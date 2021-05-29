package sovkom.api.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sovkom.api.gateway.client.phone.UserPhoneApiClient;
import sovkom.api.gateway.client.phone.dto.UserPhones;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPhonesGateway {

    private final UserPhoneApiClient userPhoneApiClient;

    public Mono<Optional<String>> getFirstUserPhone(int userId) {
        return userPhoneApiClient.getUserPhones(userId)
            .map(UserPhones::getPhones)
            .map(phones -> phones.stream().findFirst())
            .onErrorResume(__ -> Mono.just(Optional.empty()));
    }
}
