package bg.codexio.recard.apigateway.model.payload.response;

public enum TokenType {

    BEARER("Bearer ");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}