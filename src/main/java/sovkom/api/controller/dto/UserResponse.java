package sovkom.api.controller.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import sovkom.api.domain.User;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    public static int OK = 0;

    public static UserResponse TIMEOUT = error(1);
    public static UserResponse INTERNAL = error(2);

    private final String name;
    private final String phone;
    private final int code;

    public static UserResponse ok(@NonNull User user) {
        return new UserResponse(user.getName(), user.getPhone(), OK);
    }

    private static UserResponse error(int code) {
        Validate.isTrue(OK != code);
        return new UserResponse(null, null, code);
    }
}
