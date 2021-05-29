package sovkom.api.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import sovkom.api.configuration.properties.UsersProperties;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(UsersProperties.class)
public class SecurityConfiguration {

    @Bean
    public MapReactiveUserDetailsService userDetailsService(UsersProperties properties) {
        var userDetails = properties.getUsers().entrySet().stream()
            .map(entry ->
                User.withUsername(entry.getKey())
                    .password(entry.getValue().getPassword())
                    .roles(entry.getValue().getRole())
                    .build()
            )
            .collect(Collectors.toMap(UserDetails::getUsername, Function.identity()));
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().authenticated()
            )
            .httpBasic(withDefaults())
            .build();
    }
}
