package sovkom.api.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "web-client")
public class WebClientProperties {
    private int connectionTimeoutMs;
    private int readTimeoutMs;
    private int writeTimeoutMs;
}

