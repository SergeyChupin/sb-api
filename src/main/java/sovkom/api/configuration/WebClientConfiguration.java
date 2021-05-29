package sovkom.api.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import sovkom.api.configuration.properties.WebClientProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebClient.Builder webClientBuilder(ReactorClientHttpConnector httpConnector) {
        return WebClient.builder().clientConnector(httpConnector);
    }

    @Bean
    public ReactorNettyHttpClientMapper timeoutNettyClientCustomizer(WebClientProperties properties) {
        return httpClient -> httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeoutMs())
            .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(properties.getWriteTimeoutMs(), TimeUnit.MILLISECONDS));
                }
            );
    }
}
