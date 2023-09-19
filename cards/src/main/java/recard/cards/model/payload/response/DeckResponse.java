package recard.cards.model.payload.response;

public record DeckResponse(Long id,
                           Long playerId,
                           String name,
                           Integer capacity) {
}