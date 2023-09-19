package recard.cards.model.payload.request;

import java.util.List;

public record RemoveCardsRequest(List<Long> ids) {
}