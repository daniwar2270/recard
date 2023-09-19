package bg.codexio.recard.auth.model.payload.event;

import bg.codexio.recard.auth.annotations.KafkaPayload;

@KafkaPayload
public record UserIdEvent(Long id) {
}