package sovkom.api.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.hilariousstartups.soap.gen.GetUserRequest;
import ru.hilariousstartups.soap.gen.GetUserResponse;
import ru.hilariousstartups.soap.gen.User;
import sovkom.api.gateway.client.user.UserApiClient;

@Service
@RequiredArgsConstructor
public class UserApiGateway {

    private final UserApiClient userApiClient;

    public Mono<String> getUserName(int id) {
        return userApiClient.getUser(createRequest(id))
            .map(GetUserResponse::getUser)
            .map(this::createUserName);
    }

    private GetUserRequest createRequest(int id) {
        GetUserRequest request = new GetUserRequest();
        request.setUserId(id);
        return request;
    }

    private String createUserName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
