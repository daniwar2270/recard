package recard.cards.model.payload.response.events;

import recard.cards.annotation.KafkaPayload;

@KafkaPayload
public record UserIdEvent(Long id) {
}