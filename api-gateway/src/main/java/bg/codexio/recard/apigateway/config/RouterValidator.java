package bg.codexio.recard.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

@Service
public class RouterValidator {

    private Set<String> noAuthPaths;

    public RouterValidator(ApiProperties apiProperties) {
        this.noAuthPaths = apiProperties.getOpenRequestPaths();
    }

    public Predicate<ServerHttpRequest> isSecured = request ->
            this.noAuthPaths
                    .stream()
                    .noneMatch(uri -> request
                            .getURI()
                            .getPath()
                            .equals(uri)
                    );
}