package sovkom.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private final String name;
    private final String phone;
}
