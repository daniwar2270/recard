package recard.cards.model.payload.request;

import jakarta.validation.constraints.Pattern;
import recard.cards.constant.ErrorConstants;
import recard.cards.constant.RegexConstants;

public record RenameDeckRequest(

    @Pattern(regexp = RegexConstants.DECK_NAME_REGEX, message = ErrorConstants.INVALID_DECK_NAME)
    String name
) {
}
