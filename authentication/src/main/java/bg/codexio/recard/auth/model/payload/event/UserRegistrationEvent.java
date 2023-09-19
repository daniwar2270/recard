package bg.codexio.recard.auth.model.payload.event;

import bg.codexio.recard.auth.annotations.KafkaPayload;

import java.time.LocalDate;

@KafkaPayload
public record UserRegistrationEvent(Long id, String firstName, String lastName, LocalDate bornOn) {
}