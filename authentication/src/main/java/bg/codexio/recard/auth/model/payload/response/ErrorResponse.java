package bg.codexio.recard.auth.model.payload.response;

import java.util.List;

public record ErrorResponse(List<FieldError> errors) {}