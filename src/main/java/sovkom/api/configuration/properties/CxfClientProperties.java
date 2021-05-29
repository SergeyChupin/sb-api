package sovkom.api.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cxf-client")
public class CxfClientProperties {
    private int connectionTimeoutMs;
    private int receiveTimeoutMs;
}