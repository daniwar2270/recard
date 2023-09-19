package recard.cards.model.payload.request;

import java.util.List;

public record CardsToDeckRequest(List<Long> cardIds) {
}
