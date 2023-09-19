package bg.codexio.recard.filesmanagement.model.payload.event;

import bg.codexio.recard.filesmanagement.config.KafkaPayload;

@KafkaPayload
public record ImageUploadEvent(String userId, String imageUrl, String thumbnailUrl) {}