package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class CanNotCreateDeck extends RuntimeException {

    public CanNotCreateDeck() {
        super(ErrorConstants.CAN_NOT_CREATE_DECK);
    }
}
