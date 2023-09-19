package recard.cards.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConstants {

    public static final String CRITERIA_REGEX = "[\\[\\]\"\\s]";
    public static final String DECK_NAME_REGEX = "^[A-Za-z0-9\\-_ ]{3,30}$";
}