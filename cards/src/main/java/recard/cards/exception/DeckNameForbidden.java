package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class DeckNameForbidden extends RuntimeException {

    public DeckNameForbidden() {
        super(ErrorConstants.DECK_NAME_FORBIDDEN);
    }
}
