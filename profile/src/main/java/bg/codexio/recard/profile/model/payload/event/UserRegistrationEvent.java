package bg.codexio.recard.profile.model.payload.event;

import bg.codexio.recard.profile.annotation.KafkaPayload;

import java.time.LocalDate;

@KafkaPayload
public record UserRegistrationEvent(Long id, String firstName, String lastName, LocalDate bornOn) {
}