package sovkom.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sovkom.api.domain.User;
import sovkom.api.gateway.UserApiGateway;
import sovkom.api.gateway.UserPhonesGateway;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserApiGateway userApiGateway;
    private final UserPhonesGateway userPhonesGateway;

    public Mono<User> getUser(int id) {
        return Mono.zip(userApiGateway.getUserName(id), userPhonesGateway.getFirstUserPhone(id))
            .map(userInfo ->
                User.builder()
                    .name(userInfo.getT1())
                    .phone(userInfo.getT2().orElse(null))
                    .build()
            );
    }
}
