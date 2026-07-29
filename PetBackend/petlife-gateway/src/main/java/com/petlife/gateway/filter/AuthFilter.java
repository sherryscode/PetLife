package com.petlife.gateway.filter;

import com.petlife.common.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/user/login",
            "/auth/user/register",
            "/auth/merchant/login",
            "/auth/merchant/register",
            "/auth/admin/login",
            "/swagger-ui/",
            "/swagger-resources/",
            "/v3/api-docs/"
    );

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            String token = request.getHeaders().getFirst("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            token = token.substring(7);
            if (!JwtUtil.validateToken(token)) {
                return unauthorized(exchange);
            }

            Long userId = JwtUtil.getUserIdFromToken(token);
            String role = JwtUtil.getRoleFromToken(token);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId.toString())
                    .header("X-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
    }
}