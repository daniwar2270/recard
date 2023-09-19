package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class DeckOverSizeLimitException extends RuntimeException {

    public DeckOverSizeLimitException() {
        super(ErrorConstants.DECK_OVER_SIZE_LIMIT);
    }
}