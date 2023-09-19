package bg.codexio.recard.apigateway.config;

import bg.codexio.recard.apigateway.constant.FilterOrderingConstants;
import bg.codexio.recard.apigateway.constant.HeaderConstants;
import bg.codexio.recard.apigateway.constant.RedisConstants;
import bg.codexio.recard.apigateway.model.payload.response.TokenType;
import bg.codexio.recard.apigateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(FilterOrderingConstants.AUTHORIZATION_FILTER_ORDER)
public class CustomAuthenticationFilter implements GlobalFilter {

    private final RouterValidator validator;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final var request = exchange.getRequest();
        if (!this.validator.isSecured.test(request)) {
            return chain.filter(exchange);
        }

        if (this.isAuthHeaderMissing(request)) {
            return this.sendError(exchange);
        }

        final var authHeader = request.getHeaders().getOrEmpty(HeaderConstants.AUTHORIZATION_HEADER).get(0);
        final var tokenType = TokenType.BEARER.getValue();
        final var indexToRemoveTokenType = tokenType.length();
        final var token = authHeader.substring(indexToRemoveTokenType);
        final var hashOperations = this.redisTemplate.opsForHash();
        final var setOperations = this.redisTemplate.opsForSet();

        if (hashOperations.hasKey(RedisConstants.WHITELIST, token)) {
            final var userId = Objects.requireNonNull(hashOperations.get(RedisConstants.WHITELIST, token)).toString();
            final var mutatedExchange = this.mutateServerWebExchange(exchange, userId);

            return chain.filter(mutatedExchange);
        }

        if (Boolean.TRUE.equals(setOperations.isMember(RedisConstants.BLACKLIST, token))) {
            return this.sendError(exchange);
        }

        final var isTokenValid = this.validateToken(token, tokenType, authHeader);
        if (!isTokenValid) {
            return this.addTokenToBlackList(setOperations, token, exchange);
        }

        return this.getUserId(token)
                .map(userId -> chain.filter(this.mutateServerWebExchange(exchange, userId)))
                .orElseGet(() -> this.addTokenToBlackList(setOperations, token, exchange));
    }

    private Mono<Void> addTokenToBlackList(SetOperations<String, String> setOperations, String token, ServerWebExchange exchange) {
        setOperations.add(RedisConstants.BLACKLIST, token);

        return this.sendError(exchange);
    }

    private boolean isAuthHeaderMissing(ServerHttpRequest request) {
        return !request
                .getHeaders()
                .containsKey(HeaderConstants.AUTHORIZATION_HEADER);
    }

    private Mono<Void> sendError(ServerWebExchange exchange) {
        final var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    private ServerWebExchange mutateServerWebExchange(ServerWebExchange exchange, String userId) {
        final var mutatedRequest = exchange
                .getRequest()
                .mutate()
                .headers(headers -> headers.add(HeaderConstants.USER_ID_HEADER, userId))
                .build();

        return exchange
                .mutate()
                .request(mutatedRequest)
                .build();
    }

    private boolean validateToken(String token, String tokenType, String authHeader) {
        try {
            if (this.jwtService.isTokenExpired(token) || !authHeader.startsWith(tokenType)) {
                return false;
            }
        } catch (JwtException e) {
            e.printStackTrace();
        }

        return true;
    }

    private Optional<String> getUserId(String token) {
        try {
            return Optional.ofNullable(this.jwtService.extractId(token));
        } catch (JwtException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}