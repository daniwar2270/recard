package bg.codexio.recard.auth.model.payload.response;

public record AuthenticationResponse(String accessToken, String refreshToken) {
}