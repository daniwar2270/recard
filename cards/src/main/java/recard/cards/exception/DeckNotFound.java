package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class DeckNotFound extends RuntimeException {

    public DeckNotFound() {
        super(ErrorConstants.DECK_NOT_FOUND);
    }
}