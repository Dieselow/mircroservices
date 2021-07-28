package fr.esgi.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r
                        .path("/trading/**")
                        .filters(p -> p.filter(filter))
                        .uri("http://localhost:3001/trading"))
                .route(r -> r
                        .path("/users/**")
                        .filters(p -> p.filter(filter))
                        .uri("http://localhost:3002/users"))
                .route(r -> r
                        .path("/auth/**")
                        .filters(p -> {
                            p.filter(filter);
//                            p.requestRateLimiter()
//                                    .rateLimiter(RedisRateLimiter.class,
//                                            rl -> rl.setBurstCapacity(40).setReplenishRate(20));
                            return p;
                        })
                        .uri("http://localhost:3003/auth"))
                .build();
    }


}