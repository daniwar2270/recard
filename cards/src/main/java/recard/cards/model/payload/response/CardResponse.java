package recard.cards.model.payload.response;

public record CardResponse (Long id,
                            String name,
                            String description,
                            Integer attack,
                            Integer defence,
                            String imageUrl,
                            String rarity) {
}