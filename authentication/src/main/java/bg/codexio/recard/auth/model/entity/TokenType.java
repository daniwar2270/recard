package bg.codexio.recard.auth.model.entity;

public enum TokenType {
    BEARER("Bearer ");
    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}