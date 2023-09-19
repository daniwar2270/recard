package bg.codexio.recard.profile.model.payload.event;

import bg.codexio.recard.profile.annotation.KafkaPayload;

@KafkaPayload
public record ImageUploadEvent(Long userId, String imageUrl, String thumbnailUrl) {
}
