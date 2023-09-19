package bg.codexio.recard.auth.controller;

import bg.codexio.recard.auth.model.payload.request.AuthenticationRequest;
import bg.codexio.recard.auth.model.payload.response.RefreshTokenResponse;
import bg.codexio.recard.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<RefreshTokenResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        final var authenticationResponse = this.authService.login(authenticationRequest);
        final var headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.accessToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(RefreshTokenResponse.fromAuthenticationResponse(authenticationResponse));
    }

    @PutMapping("/access-token")
    public ResponseEntity<RefreshTokenResponse> renewToken(@RequestHeader("Authorization") String header) {
        final var authenticationResponse = this.authService.refreshToken(header);
        final var headers = new HttpHeaders();
        headers.setBearerAuth(authenticationResponse.accessToken());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .headers(headers)
                .body(RefreshTokenResponse.fromAuthenticationResponse(authenticationResponse));
    }
}