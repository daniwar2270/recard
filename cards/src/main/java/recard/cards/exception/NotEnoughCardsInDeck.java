package recard.cards.exception;

import recard.cards.constant.ErrorConstants;

public class NotEnoughCardsInDeck extends RuntimeException {

    public NotEnoughCardsInDeck() {
        super(ErrorConstants.NOT_ENOUGH_CARDS_DECK);
    }
}