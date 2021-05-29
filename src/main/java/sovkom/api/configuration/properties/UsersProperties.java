package sovkom.api.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties
public class UsersProperties {
    private Map<String, UserProperties> users = new HashMap<>();

    @Data
    public static class UserProperties {
        private String password;
        private String role;
    }
}
