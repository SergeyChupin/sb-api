package sovkom.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sovkom.api.controller.dto.UserResponse;
import sovkom.api.service.UserService;

import java.net.SocketTimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Mono<UserResponse> call(@PathVariable("id") int id) {
        return userService.getUser(id)
            .map(UserResponse::ok)
            .onErrorResume(this::handleError);
    }

    private Mono<UserResponse> handleError(Throwable error) {
        log.error("Unable process request", error);
        return Mono.just(
            ExceptionUtils.throwableOfType(error, SocketTimeoutException.class) != null
                ? UserResponse.TIMEOUT
                : UserResponse.INTERNAL
        );
    }
}
