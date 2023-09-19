package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class CardNotFound extends RuntimeException {

    public CardNotFound() {
        super(ErrorConstants.CARD_NOT_FOUND);
    }
}