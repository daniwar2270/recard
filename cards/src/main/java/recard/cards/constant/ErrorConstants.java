package recard.cards.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
    public static final String DECK_NOT_FOUND = "Deck not found.";
    public static final String DECK_OVER_SIZE_LIMIT = "Cannot add more cards to deck.";
    public static final String DECK_NOT_AVAILABLE = "Deck is not available to users other than its owner.";
    public static final String CAN_NOT_CREATE_DECK = "Deck cannot be created.";
    public static final String CARD_NOT_FOUND = "Card not found";
    public static final String INVALID_DECK_NAME = "Invalid format for deck name. Name must be between 3 and 30 " +
            "characters and can only contain letters, numbers, hyphens, underscores and spaces.";
    public static final String DECK_NAME_FORBIDDEN = "Deck name is forbidden.";
    public static final String NOT_ENOUGH_CARDS_DECK = "You do not have enough cards to be removed.";
}